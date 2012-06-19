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
  

	private static IRecordPresenter instance = new SWTLabelPresenter();
  
	
	public static IRecordPresenter getInstance()
	{
		return instance;
	}
	
	  private SWTLabelPresenter()
	  {
		  new Thread(this).start();
	  }
	  
	  private Shell shell;
	  private Label label;
	   
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
			label.setSize(100,30);
			label.setLocation(50, 50);
			label.setText("I am a Label");
		 
			Label shadow_sep_h = new Label(shell, SWT.SEPARATOR | SWT.SHADOW_OUT | SWT.HORIZONTAL);
			shadow_sep_h.setBounds(50,80,100,50);
		 
			Label shadow_sep_v = new Label(shell, SWT.SEPARATOR | SWT.SHADOW_IN | SWT.VERTICAL);
			shadow_sep_v.setBounds(50,100,5,100);
		 
		 
			shell.setSize(300,300);
			shell.setVisible(false);
			shell.open ();
			while (!shell.isDisposed ()) {
				if (!display.readAndDispatch ()) display.sleep ();
			}
			display.dispose ();
		}
		  
		public static void main(String[] args)
		{
			SWTLabelPresenter presenter = new SWTLabelPresenter();
			presenter.run();
		}
		  
  

}
