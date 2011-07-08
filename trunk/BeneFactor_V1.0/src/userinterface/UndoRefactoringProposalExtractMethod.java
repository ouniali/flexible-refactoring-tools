package userinterface;

import java.io.File;

import JavaRefactoringAPI.JavaUndoRefactoring;

public class UndoRefactoringProposalExtractMethod extends UndoRefactoringProposal{

	public UndoRefactoringProposalExtractMethod(JavaUndoRefactoring u) {
		super(u);
	}

	@Override
	public String getDisplayString() {
		return "Undo extract method refactoring";
	}

	@Override
	protected String getImageFileName() {
		return "undo_rename.jpg";
	}

}
