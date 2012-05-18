package userinterface;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;

import util.ASTUtil;

import compilation.UndoRefactoringChances;

import JavaRefactoringAPI.JavaRefactoringType;
import JavaRefactoringAPI.JavaUndoRefactoring;

public class UndoRefactoringQuickFixProcessor implements IQuickFixProcessor {

	@Override
	public boolean hasCorrections(ICompilationUnit unit, int problemId) {
		return true;
	}

	@Override
	public IJavaCompletionProposal[] getCorrections(IInvocationContext context,
			IProblemLocation[] locations) throws CoreException 
	{
		ICompilationUnit unit = context.getCompilationUnit();
		CompilationUnit tree = ASTUtil.parseICompilationUnit(unit);
		int selection = context.getSelectionOffset();
		int line = tree.getLineNumber(selection);
		ArrayList<JavaUndoRefactoring> undos = UndoRefactoringChances.getUndoRefactoring(unit, line);
		IJavaCompletionProposal[] proposals = new IJavaCompletionProposal[undos.size()];
		for(int i = 0; i< proposals.length; i++)
			proposals[i] =  getUndoRefactoringProposal(undos.get(i));
		return proposals;
	}
	
	public IJavaCompletionProposal getUndoRefactoringProposal(JavaUndoRefactoring undo)
	{
		int type = undo.getRefactoringType();
		switch(type)
		{
		case JavaRefactoringType.RENAME:
			return new UndoRefactoringProposalRename(undo);
		case JavaRefactoringType.EXTRACT_METHOD:	
			return new UndoRefactoringProposalExtractMethod(undo);
		case JavaRefactoringType.EXTRACT_LOCAL_VARIABLE:
			return new UndoRefactoringProposalExtractLocalVariable(undo);
		default:
			return null;
		}
	}

}
