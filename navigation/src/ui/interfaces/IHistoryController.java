// Copyright 2012 Google Inc. All Rights Reserved.
package ui.interfaces;

import history.interfaces.ICompilationUnitHistoryRecord;

import history.interfaces.ICompilationUnitHistory;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public interface IHistoryController{
  void setHistory(ICompilationUnitHistory history);
  ICompilationUnitHistory getHistory();
  void navigateTo(double scale);
  void dismiss();
  void show();
}

