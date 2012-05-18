package userinterface;

import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import JavaRefactoringAPI.JavaUndoRefactoring;

public class UndoRefactoringProposalExtractLocalVariable extends UndoRefactoringProposal {

	public UndoRefactoringProposalExtractLocalVariable(JavaUndoRefactoring u) {
		super(u);
	}

	@Override
	public String getDisplayString() {
		return "Undo extract local variable refactoring";
	}

	@Override
	protected String getImageFileName() {
		// TODO Auto-generated method stub
		return "undo_rename.jpg";
	}

	

}
