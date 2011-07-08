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
import JavaRefactoringAPI.JavaRefactoringRename;

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
		int selection = context.getSelectionOffset();
		int line = tree.getLineNumber(selection);
		ArrayList<JavaRefactoring> refactorings = RefactoringChances.getJavaRefactoring(unit, line);
		int size = refactorings.size();
		IJavaCompletionProposal[] results = new IJavaCompletionProposal[size];
		for(int i = 0; i< size; i++)
			results[i] = getRefactoringProposalRefactoring(refactorings.get(i));	
		return results;
	}
	
	public static RefactoringProposal getRefactoringProposalRefactoring(JavaRefactoring ref)
	{
		if(ref instanceof JavaRefactoringRename)
			return new RefactoringProposalRename(ref);
		else if ( ref instanceof JavaRefactoringExtractMethod)
			return new RefactoringProposalExtractMethod(ref);
		else
			return null;
	}

}
