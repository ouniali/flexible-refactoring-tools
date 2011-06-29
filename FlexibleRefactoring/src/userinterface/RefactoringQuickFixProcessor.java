package userinterface;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;

public class RefactoringQuickFixProcessor implements IQuickFixProcessor {

	int refactoringType;
	RefactoringProposal proposal;
	
	@Override
	public boolean hasCorrections(ICompilationUnit unit, int problemId) {
		
		return problemId == IProblem.ExternalProblemFixable;
	}

	@Override
	public IJavaCompletionProposal[] getCorrections(IInvocationContext context,
			IProblemLocation[] locations) throws CoreException {
		
		if (locations == null || locations.length == 0) {
           return null;
		}
		return  new IJavaCompletionProposal[]{new RefactoringProposalRename()};
	/*	for (IProblemLocation location: locations)
		{
			if(location.getProblemId() == IProblem.ExternalProblemFixable)
			{
				String[] parameters = location.getProblemArguments();
				if(parameters[0].equals(RefactoringMarker.Refactoring_Problem_First_Argument))
				{
					refactoringType = Integer.parseInt(parameters[1]);
					proposal = RefactoringProposal.getRefactoringProposalByType(refactoringType);
					return new IJavaCompletionProposal[]{proposal};
				}
			}
		}
		
		return null;*/
	}

}
