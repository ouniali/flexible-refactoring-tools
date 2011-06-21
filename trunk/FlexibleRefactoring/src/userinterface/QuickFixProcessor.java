package userinterface;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;
import org.eclipse.ui.IMarkerResolution;

public class QuickFixProcessor implements IQuickFixProcessor {

	@Override
	public boolean hasCorrections(ICompilationUnit unit, int problemId) {
		// TODO Auto-generated method stub
		if(problemId == IProblem.ExternalProblemFixable)
			return true;
		else
			return false;
	}

	@Override
	public IJavaCompletionProposal[] getCorrections(IInvocationContext context,
			IProblemLocation[] locations) throws CoreException {
		// TODO Auto-generated method stub
		return new IJavaCompletionProposal[] {
				new CompletionRefactoringProposal()
		};
	}

}
