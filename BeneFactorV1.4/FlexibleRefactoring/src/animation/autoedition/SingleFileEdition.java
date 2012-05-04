package animation.autoedition;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.junit.Assert;

import animation.autoedition.ui.ScalingBar;

import util.UIUtil;


public class SingleFileEdition extends Observable implements Runnable, Observer{

	final private static int NO_REPOSITION = -1; 
	
	final public static int PAUSE = 1;
	final public static int RESUME = 2;
	final public static int FASTER = 3;
	final public static int SLOWER = 4;
	
	final int[] SPEED = {20, 100, 500, 1000, 2000, 2500, 3000};//in millisecond
	int interval_index = SPEED.length/2;
	

	
	ArrayList<AtomicEdition> editions;	
	ArrayList<AtomicEdition> undos = new ArrayList<AtomicEdition>();

	
	int current_applied = 0;//number of atomic editions that were played 
	ICompilationUnit unit; //these editions are applied on
	AtomicInteger set_finished_count = new AtomicInteger(SingleFileEdition.NO_REPOSITION);//value set from scaling bar
	Thread playing_thread;//playing thread
	JavaEditor editor;//the editor in which this compilation unit is open
	boolean editor_open;//is the editor originally open 
	
	
	
	public SingleFileEdition()
	{
		super();
		editions = new ArrayList<AtomicEdition>();	
	}
	
	private SingleFileEdition(ArrayList<AtomicEdition> eds)
	{
		super();
		editions = eds;	
	}
	
	public void addEdition(AtomicEdition e)
	{
		editions.add(e);
	}
	
	
	public void addAllEditions(ArrayList<AtomicEdition> eds)
	{
		editions.addAll(eds);
	}

	
	@Override
	public void run() {
	
		try {			
			
			unit.becomeWorkingCopy(null);
			openEditor();
			connect2ScalingBar();
			
			for(;playNextAtomicEdition();)
			{
				int finished_count = set_finished_count.getAndSet(SingleFileEdition.NO_REPOSITION);
				if(finished_count !=  SingleFileEdition.NO_REPOSITION)
					AdjustEditionsProgress(finished_count);		
				Thread.sleep(SPEED[interval_index]);
			}
			
			disconnectScalingBar();
			closeEditor();
			unit.commitWorkingCopy(true, null);
			unit.discardWorkingCopy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void waitFinish() throws Exception
	{
		playing_thread.join();
	}
	

	private void openEditor() throws Exception
	{
		editor = UIUtil.getJavaEditor(unit);
		if(editor == null)
		{
			editor = UIUtil.openJavaEditor(unit);
			editor_open = false;
		}
		else
			editor_open = true;
		
	}

	private void closeEditor()
	{
		if(!editor_open)
			UIUtil.closeJavaEditor(editor);
	}
	
	private void connect2ScalingBar() {
		ScalingBar.getInstance().setPercentage(0.0f);
		ScalingBar.getInstance().addObserver(this);
		this.addObserver(ScalingBar.getInstance());
	}
	
	private void disconnectScalingBar() {
		ScalingBar.getInstance().deleteObserver(this);
		this.deleteObserver(ScalingBar.getInstance());
	}
	
	

	
	void AdjustEditionsProgress(int finished) throws Exception 
	{
		if(finished > current_applied)
			jumpForward(finished - current_applied);
		else if(finished < current_applied)
			jumpBackward(current_applied - finished);
		else 
			return;
	}
	
	void jumpForward(int step) throws Exception
	{
	/*	System.out.println("before jump.");
		for(AtomicEdition ee: editions)
			System.out.println(ee);
	*/	
		AtomicEdition e = AtomicEdition.mergeConsecutiveAtomicEditionsTop2Bottom
				(editions, current_applied, current_applied + step - 1);
		current_applied = current_applied + step;
		e.applyEdition(unit);
		AtomicEdition undo = e.getUndoAtomicEdition();
		ArrayList<AtomicEdition> undo_elements = undo.splitToAtomicEditions();
		if(undo_elements.size() != step)
			throw new Exception("Inconsistency");
		undos.addAll(0, undo_elements);
		
/*	System.out.println("after jump.");
		for(AtomicEdition ee: editions)
			System.out.println(ee);*/
	}
	
	void jumpBackward(int step) throws Exception
	{
		AtomicEdition e = AtomicEdition.mergeConsecutiveAtomicEditionsBottom2Top
			(undos, 0, step - 1);
		current_applied -= step;
		e.applyEdition(unit);
		for(int i = 0; i<step; i++ )
			undos.remove(0);
	}
	
	
	public void setICompilationUnit(ICompilationUnit u)
	{
		unit = u;
	}
	
	public void applyEditions() throws Exception
	{
		if(unit == null)
			throw new Exception("ICompilationUnit is null");
		playing_thread = new Thread(this);
		playing_thread.start();
	}
	
	
	boolean playNextAtomicEdition()
	{
		if(current_applied >= editions.size())
			return false;
	
		try {
			AtomicEdition e = editions.get(current_applied);
			current_applied ++;
			e.applyEdition(unit);
			undos.addAll(0, e.getUndoAtomicEdition().splitToAtomicEditions());
			System.out.println("Play Next: " + e);
			synchScalingBar();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return true;
	}
	
	
	boolean undoLatestEdition()
	{
		if(current_applied <= 0)
			return false;
		try{
			AtomicEdition e =  undos.remove(0);
			current_applied--;
			e.applyEdition(unit);
			synchScalingBar();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return true;
	}
	
	public SingleFileEdition getUndoEditionComposite()
	{		
		if(undos.size() == editions.size())
			return new SingleFileEdition(undos);
		else 
			return null;
	}
	
	public void setFinishedScale(float scale) throws Exception
	{
		Assert.assertTrue(scale >= 0.0f && scale <= 1.0f);
		int finishedNumber = (int)( scale * (float)editions.size());
		set_finished_count.set(finishedNumber);
	}

	
	private void synchScalingBar()
	{
		float scale = (float)current_applied;
		float total = (float)editions.size();
		scale = scale/total;
		setChanged();
		notifyObservers(new Float(scale));
	}

	@Override
	public void update(Observable o, Object arg) {
		//System.out.println("update_composite " + (Float)arg);
		if(arg instanceof Float)
		{
			float per = ((Float)arg).floatValue();
			try {
				setFinishedScale(per);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (arg instanceof Integer)
		{
			int mes = ((Integer) arg).intValue();
			handleButtonMessage(mes);
		}
	}
	
	void handleButtonMessage(int code)
	{
		switch(code)
		{
		case SingleFileEdition.PAUSE:
			playing_thread.suspend();
			break;
		case SingleFileEdition.RESUME:
			playing_thread.resume();
			break;
		case SingleFileEdition.SLOWER:
			if(interval_index + 1 < SPEED.length)
				interval_index ++;
			break;
		case SingleFileEdition.FASTER:
			if(interval_index - 1 >= 0)
				interval_index --;
			break;		
		default:
			break;
		}
		
	}
	

	public void adjustToSynchronizedApply() throws Exception
	{
		Collections.sort(editions);
		int shift = 0;
		for(AtomicEdition ed : editions)
		{
			int off = ed.getOffset();
			int new_off = off + shift;
			ed.setOffset(new_off);
			shift += ed.getRangeChange();
		}
	}
	
	public void splitEditions() throws Exception
	{
		ArrayList<AtomicEdition> new_editions = new ArrayList<AtomicEdition>();
		for(AtomicEdition e : editions)
			new_editions.addAll(e.splitToAtomicEditions());
		editions = new_editions;
		undos.clear();
	}
	
	


}
