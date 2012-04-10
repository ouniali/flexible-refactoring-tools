package animation.autoedition;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Assert;


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
	ICompilationUnit unit;
	AtomicInteger set_finished_count = new AtomicInteger(SingleFileEdition.NO_REPOSITION);
	Thread playing_thread;
	
	
	
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
	public synchronized void run() {
	
		try {
			unit.becomeWorkingCopy(null);
		} catch (JavaModelException e1) {
			e1.printStackTrace();
		}
		
		openEditor();
		connect2ScalingBar();
		
		for(;playNextAtomicEdition();)
		{
			int finished_count = set_finished_count.getAndSet(SingleFileEdition.NO_REPOSITION);
			if(finished_count !=  SingleFileEdition.NO_REPOSITION)
				AdjustEditionsProgress(finished_count);
			try {
				Thread.sleep(SPEED[interval_index]);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		disconnectScalingBar();
		closeEditor();
		
		try {
			unit.commitWorkingCopy(true, null);
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void openEditor()
	{
		
	}

	private void closeEditor()
	{
		
	}
	
	private void connect2ScalingBar() {
		ScalingBar.getInstance().addObserver(this);
		this.addObserver(ScalingBar.getInstance());
	}
	
	private void disconnectScalingBar() {
		ScalingBar.getInstance().deleteObserver(this);
		this.deleteObserver(ScalingBar.getInstance());
	}
	
	

	
	synchronized void AdjustEditionsProgress(int finished) 
	{
			if(finished > current_applied)
			{
				while(finished != current_applied)
					playNextAtomicEdition();
			}
			else if(finished < current_applied)
			{
				while(finished != current_applied)
					undoLatestEdition();
			}	
			else 
				return;
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
	
	
	synchronized boolean playNextAtomicEdition()
	{
		if(current_applied >= editions.size())
			return false;
	
		try {
			AtomicEdition e = editions.get(current_applied);
			current_applied ++;
			e.applyEdition(unit);
			undos.add(0, e.getUndoAtomicEdition());	
			synchScalingBar();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return true;
	}
	
	
	synchronized boolean undoLatestEdition()
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
