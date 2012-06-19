// Copyright 2012 Google Inc. All Rights Reserved.

package ui.implementations;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import history.interfaces.ICompilationUnitHistoryRecord;

import ui.interfaces.IRecordPresenter;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public class SWTLabelPresenter implements IRecordPresenter, Runnable{
  

	private static IRecordPresenter instance;
  
	
	public static IRecordPresenter getInstance()
	{
		if(instance == null)
			instance = new SWTLabelPresenter();
		return instance;
	}
	
	  private SWTLabelPresenter()
	  {
		  new Thread(this).start();
	  }
	  
	  private Shell shell;
	  private Label label;
	  private int width = 400;
	  
	  @Override
	  public void close() {
	    shell.setVisible(false);
	  }
	
	  @Override
	  public void present(ICompilationUnitHistoryRecord record) throws Exception {
		  String source = record.getSourceCode();
		  label.setText(source);
		  shell.setVisible(true);
	  }
	
		@Override
		public void run() {
			Display display = new Display ();
			shell = new Shell(display);
		 
			label = new Label(shell, SWT.BORDER);
			label.setSize(width, width);
			label.setLocation(0, 0);
			
			shell.setSize(width, width);
			shell.open ();
			shell.setVisible(false);

			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			display.dispose ();
		}
		  
		public static void main(String[] args)
		{
			/*SWTLabelPresenter presenter = new SWTLabelPresenter();
			presenter.run();*/
			SWTLabelPresenter.getInstance();
		}
		  
  

}
