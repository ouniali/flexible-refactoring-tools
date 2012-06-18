// Copyright 2012 Google Inc. All Rights Reserved.

package history.interfaces;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public interface IHistoryCollector {
  void addHistoryRecord(ICompilationUnitHistoryRecord record);
  boolean hasHistoryForPath(String fPath);
  ICompilationUnitHistory getHistoryByPath(String fPath);
}
