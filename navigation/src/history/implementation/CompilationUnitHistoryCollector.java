// Copyright 2012 Google Inc. All Rights Reserved.

package history.implementation;

import java.util.ArrayList;
import java.util.List;

import history.interfaces.ICompilationUnitHistory;
import history.interfaces.ICompilationUnitHistoryRecord;

import history.interfaces.IHistoryCollector;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public class CompilationUnitHistoryCollector implements IHistoryCollector{

  
  private List<ICompilationUnitHistory> histories = new ArrayList<ICompilationUnitHistory> ();;
  
  private CompilationUnitHistoryCollector()
  {
    
  }

  @Override
  public void addHistoryRecord(ICompilationUnitHistoryRecord record) {
    ICompilationUnitHistory history;
    String fPath = record.getFilePath();
    if(hasHistoryForPath(fPath))
      history = getHistoryByPath(fPath);
    else
    {
      history = new CompilationUnitHistory(fPath, record.getCompilationUnitName());
      histories.add(history);
    }
    
    history.addRecord(record);
    record.setHistory(history);
  }

  @Override
  public boolean hasHistoryForPath(String fPath) {
    for(ICompilationUnitHistory history : histories)
    {
      if(history.getFilePath().equals(fPath))
        return true;
    }
    return false;
  }


  @Override
  public ICompilationUnitHistory getHistoryByPath(String fPath) {
    for(ICompilationUnitHistory history : histories)
    {
      if(history.getFilePath().equals(fPath))
        return history;
    }
    return null;
  }

}
