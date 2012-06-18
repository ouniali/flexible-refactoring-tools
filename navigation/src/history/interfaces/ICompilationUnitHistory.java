// Copyright 2012 Google Inc. All Rights Reserved.

package history.interfaces;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public interface ICompilationUnitHistory {
  void deleteRecord(ICompilationUnitHistoryRecord record);
  void addRecord(ICompilationUnitHistoryRecord record);
  String getFilePath();
  String getCompilationUnitName();
  boolean checkBelonging(ICompilationUnitHistoryRecord record);
  ICompilationUnitHistoryRecord getRecordByScale(double scale);
  int getRecordCount();
}



