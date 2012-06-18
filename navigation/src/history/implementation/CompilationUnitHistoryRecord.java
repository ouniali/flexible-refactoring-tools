// Copyright 2012 Google Inc. All Rights Reserved.

package history.implementation;

import org.eclipse.jdt.core.ICompilationUnit;
import java.io.IOException;
import util.FileUtil;
import history.interfaces.ICompilationUnitHistory;
import history.interfaces.ICompilationUnitHistoryRecord;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public class CompilationUnitHistoryRecord implements ICompilationUnitHistoryRecord{

  private final String filePath;
  private final String recordPath;
  private final String cuName;
  private ICompilationUnitHistory history;
  private final long time;
  
  public static ICompilationUnitHistoryRecord createCompilationUnitHistoryRecord(ICompilationUnit unit) throws IOException
  {
    
    String fPath = unit.getPath().makeAbsolute().toString();
    long time = System.currentTimeMillis();
    String cuName = unit.getElementName().replaceAll(".java", "");
    String rPath = FileUtil.createTempFile(cuName, String.valueOf(time));  
    FileUtil.copyfile(fPath, rPath);
    System.out.println(rPath);
    return new CompilationUnitHistoryRecord(fPath, rPath, cuName, time);
  }
  
  private CompilationUnitHistoryRecord(String fPath, String rPath, String cuName, long time)
  {
      this.filePath = fPath;
      this.recordPath = rPath;
      this.cuName = cuName;
      this.time = time;
  }

  @Override
  public ICompilationUnitHistory getHistory() {
    return history;
  }

  @Override
  public String getCompilationUnitName() {
    return cuName;
  }

  @Override
  public String getSourceCode() throws Exception {
    return FileUtil.readFileAsString(recordPath);
  }

  @Override
  public String getRecordFilePath() {
    return recordPath;
  }

  @Override
  public String getFilePath() {
    return filePath;
  }

  @Override
  public long getTime() {
    return time;
  }

  @Override
  public void open() throws Exception{
   
    
  }

  @Override
  public void setHistory(ICompilationUnitHistory history) {
    this.history = history;
  }
  
  @Override
  public boolean equals(Object o)
  {
      String rPath = ((ICompilationUnitHistoryRecord) o).getRecordFilePath();
      return this.recordPath.equals(rPath);
  }

  /* (non-Javadoc)
   * @see history.interfaces.ICompilationUnitHistoryRecord#delete()
   */
  @Override
  public void delete() throws IOException {
    FileUtil.createFile(recordPath);
  }

}
