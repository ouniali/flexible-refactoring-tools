// Copyright 2012 Google Inc. All Rights Reserved.

package ui.implementations;

import history.interfaces.ICompilationUnitHistory;

import ui.interfaces.IHistoryController;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public class HistoryScrollbar implements IHistoryController{

   ICompilationUnitHistory history;
  
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
    
  }

  @Override
  public void dismiss() {
  }

  @Override
  public void show() {
  }

}
