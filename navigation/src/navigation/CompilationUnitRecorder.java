// Copyright 2012 Google Inc. All Rights Reserved.

package navigation;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.ReconcileContext;
import history.implementation.CompilationUnitHistoryRecord;
import history.interfaces.ICompilationUnitHistoryRecord;
import history.implementation.CompilationUnitHistoryCollector;

/**
 * @author xgencsu@google.com (Xi Ge)
 *
 */
public class CompilationUnitRecorder extends CompilationParticipant {
  
  @Override
  public void reconcile(ReconcileContext context) 
  {
    
      try {
        ICompilationUnit unit = context.getWorkingCopy();
        ICompilationUnitHistoryRecord record = 
            CompilationUnitHistoryRecord.createCompilationUnitHistoryRecord(unit);
        CompilationUnitHistoryCollector.getInstance().addHistoryRecord(record);
        System.out.println("called");
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
}
