package compilation;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.ReconcileContext;

import flexiblerefactoring.BeneFactor;

public class RefactoringCompilerParticipantAddProblem extends CompilationParticipant {

	public RefactoringCompilerParticipantAddProblem() {
	}

	public boolean isActive(IJavaProject project) 
	{
		return !BeneFactor.isShutDown() && project.isOpen();
	}
	
	public void reconcile(ReconcileContext context) 
	{
		
	}

}
