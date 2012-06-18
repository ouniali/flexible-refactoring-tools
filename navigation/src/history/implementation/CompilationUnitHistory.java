// Copyright 2012 Google Inc. All Rights Reserved.

package history.implementation;
import org.eclipse.core.runtime.Assert;

import java.util.ArrayList;
import java.util.List;

import history.interfaces.ICompilationUnitHistoryRecord;

import history.interfaces.ICompilationUnitHistory;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public class CompilationUnitHistory implements ICompilationUnitHistory{

  private List<ICompilationUnitHistoryRecord> records;
  private final String cuName;
  private final String fPath;
  
  public CompilationUnitHistory(String fPath, String cuName)
  {
     records = new ArrayList<ICompilationUnitHistoryRecord>();
     this.cuName = cuName;
     this.fPath = fPath;
  }
  
  
  @Override
  public void deleteRecord(ICompilationUnitHistoryRecord record) {
    records.remove(record);
  }

  @Override
  public void addRecord(ICompilationUnitHistoryRecord record) {
    records.add(record);
  }

  @Override
  public String getCompilationUnitName() {
    return cuName;
  }

  @Override
  public boolean checkBelonging(ICompilationUnitHistoryRecord record) {
    return record.getFilePath().equals(fPath);
  }

  @Override
  public String getFilePath() {
    return fPath;
  }

  @Override
  public ICompilationUnitHistoryRecord getRecordByScale(double scale) {
    Assert.isTrue(scale > 0.0 && scale < 1.0);
    int index = (int) (getRecordCount() * scale);
    return records.get(index);
  }

  @Override
  public int getRecordCount() {
    return records.size();
  }

}