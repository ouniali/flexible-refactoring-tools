package animation.autoedition.ui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

import animation.autoedition.SingleFileEdition;

public class ScalingBar extends Observable implements Observer, Runnable{

	boolean scaling_ready = false;
	Display display;
	
	int shellX = 600;
	int shellY = 600;
	int shellWidth = 430;
	int shellHeight = 120;
	
	Scale scale;
	int minimum = 0;
	int maximum = 100;
	int page_increment = 10;
	int increment = 5;
	float percentage;
	int scaleWidth = 400;
	int scaleHeight = 80;
	
	private static ScalingBar scalingBar;
	
	static public ScalingBar getInstance()
	{
		if(scalingBar == null)
		{
			scalingBar = new ScalingBar();
		}
		return scalingBar;
	}
	
	class Barlistener implements SelectionListener
	{


		@Override
		public void widgetDefaultSelected(SelectionEvent event) {}

		@Override
		public void widgetSelected(SelectionEvent event) {
	
			ScalingBar.getInstance().setChanged();
			ScalingBar.getInstance().notifyObservers(new Float(ScalingBar.getInstance().getPercentage()));
			
		}
		
	}
	
	private ScalingBar() 
	{	  
		new Thread(this).start();
	
		while(!scaling_ready)
		try {
			Thread.sleep(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public float getPercentage()
	{
		if( !isScaleReady())
			return 0.0f;
		display.syncExec( new Runnable()
		{
			@Override
			public void run() {
				float selection = (float)scale.getSelection();
				float max = (float) scale.getMaximum();
				float min = (float) scale.getMinimum();	
				percentage = (selection-min)/(max-min);
			}
		}
		);

		return percentage;
	}
	
	public void setPercentage(float per)
	{
		if( !isScaleReady())
			return;
		percentage = per;
		if(percentage < 0.0)
			percentage = 0.0f;
		else if(percentage > 1.0)
			percentage = 1.0f;
		display.syncExec( new Runnable()
		{
			public void run() {
				float max = (float) scale.getMaximum();
				float min = (float) scale.getMinimum();
				scale.setSelection((int)(percentage * (max - min) + min));
				scale.redraw();
			}
		});
		
	}
	

	boolean isScaleReady()
	{
		return scaling_ready;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 instanceof Float)
		{
			Float p = (Float) arg1;
			setPercentage(p.floatValue());
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		 display = new Display ();
		 Shell shell = new Shell (display);
		 shell.setBounds(shellX, shellY, shellWidth, shellHeight);
		 RowLayout lay = new RowLayout();
		 lay.wrap = true;
		 lay.center = true;
		 shell.setLayout(new RowLayout());
		 Rectangle clientArea = shell.getClientArea ();
				 
		 Button pause = new Button(shell, SWT.PUSH);
		 pause.setText("Pause");
		 pause.addSelectionListener(new PauseListener());
		
		 Button resume = new Button(shell, SWT.PUSH);
		 resume.setText("Resume");
		 resume.addSelectionListener(new ResumeListener());
		
		 Button faster = new Button(shell, SWT.PUSH);
		 faster.setText("Faster");
		 faster.addSelectionListener(new FasterListener());
		 
		 Button slower = new Button(shell, SWT.PUSH);
		 slower.setText("Slower");
		 slower.addSelectionListener(new SlowerListener());
		 		
		 scale = new Scale (shell, SWT.HORIZONTAL);
		 scale.setBounds (clientArea);
		 scale.setMinimum(minimum);
		 scale.setMaximum (maximum);
		 scale.setPageIncrement (page_increment);
		 scale.setIncrement(increment);
		 scale.addSelectionListener(new Barlistener());
		 scale.setLayoutData( new RowData(scaleWidth, scaleHeight));
		 shell.open ();
		 scaling_ready = true;
		 while (!shell.isDisposed ()) {
			 if (!display.readAndDispatch ()) display.sleep ();
		 }
		 display.dispose ();
	}

	class PauseListener implements SelectionListener{

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			ScalingBar.getInstance().setChanged();
			ScalingBar.getInstance().notifyObservers(new Integer(SingleFileEdition.PAUSE));
		}
		
	}
	
	class ResumeListener implements SelectionListener{

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			ScalingBar.getInstance().setChanged();
			ScalingBar.getInstance().notifyObservers(new Integer(SingleFileEdition.RESUME));
		}
		
	}
	
	class FasterListener implements SelectionListener{

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			ScalingBar.getInstance().setChanged();
			ScalingBar.getInstance().notifyObservers(new Integer(SingleFileEdition.FASTER));
		}
		
	}
	
	class SlowerListener implements SelectionListener{

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			ScalingBar.getInstance().setChanged();
			ScalingBar.getInstance().notifyObservers(new Integer(SingleFileEdition.SLOWER));
	
		}
		
	}

	
}
