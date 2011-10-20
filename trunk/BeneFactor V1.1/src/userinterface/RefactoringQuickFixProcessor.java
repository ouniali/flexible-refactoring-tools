package userinterface;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;

import ASTree.ASTreeManipulationMethods;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringExtractMethod;
import JavaRefactoringAPI.JavaRefactoringMoveStaticMember;
import JavaRefactoringAPI.JavaRefactoringRename;
import JavaRefactoringAPI.JavaRefactoringRenameDiff;

import compilation.RefactoringChances;

public class RefactoringQuickFixProcessor implements IQuickFixProcessor {

	@Override
	public boolean hasCorrections(ICompilationUnit unit, int problemId) {
		return true;
	}

	@Override
	public IJavaCompletionProposal[] getCorrections(IInvocationContext context,
			IProblemLocation[] locations) throws CoreException {
		ICompilationUnit unit = context.getCompilationUnit();
		CompilationUnit tree = ASTreeManipulationMethods.parseICompilationUnit(unit);
		boolean get_all = false;
		int selection = context.getSelectionOffset();
		int line = tree.getLineNumber(selection);
		if(get_all)
		{
			ArrayList<JavaRefactoring> refactorings = RefactoringChances.getJavaRefactorings(unit, line);
			int size = refactorings.size();
			IJavaCompletionProposal[] results = new IJavaCompletionProposal[size];
			for(int i = 0; i< size; i++)
				results[i] = getRefactoringProposalRefactoring(refactorings.get(i));	
			return results;
		}
		else
		{
			JavaRefactoring refactoring = RefactoringChances.getLatestJavaRefactoring(unit, line);
			IJavaCompletionProposal[] result = new IJavaCompletionProposal[1];
			result[0] = getRefactoringProposalRefactoring(refactoring);
			return result;
		}
			
	}
	

	
	
	public static RefactoringProposal getRefactoringProposalRefactoring(JavaRefactoring ref)
	{
		if(ref instanceof JavaRefactoringRename || ref instanceof JavaRefactoringRenameDiff)
			return new RefactoringProposalRename(ref);
		else if ( ref instanceof JavaRefactoringExtractMethod)
			return new RefactoringProposalExtractMethod(ref);
		else if( ref instanceof JavaRefactoringMoveStaticMember)
			return new RefactoringProposalMoveStaticMember(ref);
		else
			return null;
	}

}
