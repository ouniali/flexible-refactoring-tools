package userinterface;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;

public class QuickFixProcessor implements IQuickFixProcessor {

	int refactoringType;
	RefactoringProposal proposal;
	
	@Override
	public boolean hasCorrections(ICompilationUnit unit, int problemId) {
		
		refactoringType = problemId - IProblem.ExternalProblemFixable;
		proposal = RefactoringProposal.getRefactoringProposalByType(refactoringType);
		if(proposal == null)
			return false;
		else
			return true;
	}

	@Override
	public IJavaCompletionProposal[] getCorrections(IInvocationContext context,
			IProblemLocation[] locations) throws CoreException {
		return new IJavaCompletionProposal[]{
				proposal
		};
	}

}
