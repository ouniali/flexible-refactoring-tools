// Copyright 2012 Google Inc. All Rights Reserved.

package ui.implementations;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import history.interfaces.ICompilationUnitHistoryRecord;

import history.interfaces.ICompilationUnitHistory;

import ui.interfaces.IHistoryController;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public class HistoryScale implements IHistoryController, Runnable, Listener{

   ICompilationUnitHistory history;
     
   private Display display = new Display();
   private Shell shell = new Shell(display);
   private Scale scale;
   private Label label;
   private Text text;
   
   
   private static IHistoryController controller;
   public static IHistoryController getInstance()
   {
     if(controller == null)
       controller = new HistoryScale();
     return controller;
   }

   private HistoryScale(){}
  
   @Override
  public void setHistory(ICompilationUnitHistory history) {
    this.history = history;
  }

  @Override
  public ICompilationUnitHistory getHistory() {
    return history;
  }

  @Override
  public void navigateTo(double scale) {
      openRecord(history.getRecordByScale(scale));
  }

  @Override
  public void dismiss() {
    
  }

  @Override
  public void show() {
    
  }
  
  
  private void openRecord(ICompilationUnitHistoryRecord record)
  {
    
  }
  
  @Override
  public void run() {
    shell.setLayout(new GridLayout(1, true));
    shell.setText("Show scale");
    label = new Label(shell, SWT.HORIZONTAL);
    label.setText("Speed:");

    scale = new Scale(shell, SWT.HORIZONTAL);
    scale.setMinimum(0);
    scale.setMaximum(50);
    scale.setIncrement(1);
    scale.setPageIncrement(10);
    
    scale.addListener(SWT.Selection, this);
    text = new Text(shell, SWT.BORDER | SWT.SINGLE);
    text.setEditable(true);
    scale.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER)); 
    shell.pack();
    shell.open();
    
    while (!shell.isDisposed()) {
    if (!display.readAndDispatch()) {
    display.sleep();
   }
    }
    
  }
  
  public static void main(String arg[])
  {
    Display.getDefault().syncExec(new HistoryScale());
  }

  @Override
  public void handleEvent(Event event) {
    
  }
  
}

