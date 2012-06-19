// Copyright 2012 Google Inc. All Rights Reserved.

package ui.interfaces;

import history.interfaces.ICompilationUnitHistoryRecord;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public interface IRecordPresenter {
  void present(ICompilationUnitHistoryRecord record);
  void close();
}
