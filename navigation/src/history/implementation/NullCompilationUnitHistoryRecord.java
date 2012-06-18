// Copyright 2012 Google Inc. All Rights Reserved.

package history.implementation;

import history.interfaces.ICompilationUnitHistory;

import java.io.IOException;

import history.interfaces.ICompilationUnitHistoryRecord;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public class NullCompilationUnitHistoryRecord implements ICompilationUnitHistoryRecord{

  
  @Override
  public ICompilationUnitHistory getHistory() {
    return new NullCompilationUnitHistory();
  }

  @Override
  public void setHistory(ICompilationUnitHistory history) {
    
  }

  @Override
  public String getCompilationUnitName() {
    return "NULL";
  }

  @Override
  public String getSourceCode() throws Exception {
    return "";
  }

  @Override
  public String getRecordFilePath() {
    return "";
  }

  @Override
  public String getFilePath() {
    return "";
  }

  @Override
  public long getTime() {
    return -1;
  }

  @Override
  public void open() throws Exception {
    
  }

  @Override
  public void delete() throws IOException {
    
  }

}
