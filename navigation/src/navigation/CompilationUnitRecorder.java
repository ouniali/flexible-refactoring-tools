// Copyright 2012 Google Inc. All Rights Reserved.

package navigation;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.ReconcileContext;

import history.implementation.HistoryWorkQueue;

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
        HistoryWorkQueue.handleNewVersion(unit);
        System.out.println("called");
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
}
