package compilation;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.ReconcileContext;

import flexiblerefactoring.BeneFactor;

public class RefactoringCompilerParticipantAddProblem extends CompilationParticipant {

	public RefactoringCompilerParticipantAddProblem() {
		// TODO Auto-generated constructor stub
	}

	public boolean isActive(IJavaProject project) 
	{
		return !BeneFactor.SHUT_DOWN && project.isOpen();
	}
	
	public void reconcile(ReconcileContext context) 
	{
		
	}

}
