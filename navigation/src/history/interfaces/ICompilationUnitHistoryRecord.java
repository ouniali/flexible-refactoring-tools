// Copyright 2012 Google Inc. All Rights Reserved.

package history.interfaces;

import java.io.IOException;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */

public interface ICompilationUnitHistoryRecord {
  ICompilationUnitHistory getHistory();
  void setHistory(ICompilationUnitHistory history);
  String getCompilationUnitName();
  String getSourceCode() throws Exception;
  String getRecordFilePath();
  String getFilePath();
  long getTime();
  void open() throws Exception;
  void delete() throws IOException;
}
