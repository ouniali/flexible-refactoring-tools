// Copyright 2012 Google Inc. All Rights Reserved.

package history.implementation;

import history.interfaces.ICompilationUnitHistoryRecord;

import history.interfaces.ICompilationUnitHistory;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public class NullCompilationUnitHistory implements ICompilationUnitHistory {

  @Override
  public void deleteRecord(ICompilationUnitHistoryRecord record) { 
  }

  @Override
  public void addRecord(ICompilationUnitHistoryRecord record) {
  
  }

  @Override
  public String getFilePath() {
    return "";
  }

  @Override
  public String getCompilationUnitName() {
    return "NULL";
  }

  @Override
  public boolean checkBelonging(ICompilationUnitHistoryRecord record) {
    return false;
  }

  @Override
  public ICompilationUnitHistoryRecord getRecordByScale(double scale) {
    return new NullCompilationUnitHistoryRecord();
  }

  @Override
  public int getRecordCount() {
    return -1;
  }

}
