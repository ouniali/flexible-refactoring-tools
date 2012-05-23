package userinterface;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;

import JavaRefactoringAPI.move.JavaRefactoringMoveStaticMember;

import JavaRefactoringAPI.rename.JavaRefactoringRename;
import JavaRefactoringAPI.rename.JavaRefactoringRenameDiff;

import util.ASTUtil;

import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;

import compilation.RefactoringChances;

public class RefactoringQuickFixProcessor implements IQuickFixProcessor {

	@Override
	public boolean hasCorrections(ICompilationUnit unit, int problemId) {
		return true;
	}

	@Override
	public IJavaCompletionProposal[] getCorrections(IInvocationContext context,
			IProblemLocation[] locations) throws CoreException {
		try{
			ICompilationUnit unit = context.getCompilationUnit();
			CompilationUnit tree = ASTUtil.parseICompilationUnit(unit);
			int selection = context.getSelectionOffset();
			int line = tree.getLineNumber(selection);
			if(RefactoringChances.getInstance().hasRefactorings(unit, line))
			{
				JavaRefactoring refactoring = RefactoringChances.getInstance().
					getLatestJavaRefactoring(unit, line);
				IJavaCompletionProposal[] result = new IJavaCompletionProposal[1];
				result[0] = getRefactoringProposalRefactoring(refactoring);
				return result;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private RefactoringProposal getRefactoringProposalRefactoring(JavaRefactoring ref)
	{
		int type = ref.getRefactoringType();
		switch(type)
		{
		case JavaRefactoringType.RENAME:
			return new RefactoringProposalRename(ref);
		case JavaRefactoringType.EXTRACT_METHOD:
			return new RefactoringProposalExtractMethod(ref);
		case JavaRefactoringType.MOVE_STATIC:
			return new RefactoringProposalMoveStaticMember(ref);
		case JavaRefactoringType.EXTRACT_LOCAL_VARIABLE:
			return new RefactoringProposalExtractLocalVariable(ref);
		default:
			break;
		}
		try{
			throw new Exception("unknown refactoring type.");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
