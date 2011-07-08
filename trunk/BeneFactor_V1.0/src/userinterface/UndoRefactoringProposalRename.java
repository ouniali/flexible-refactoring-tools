package userinterface;

import java.io.File;

import JavaRefactoringAPI.JavaUndoRefactoring;

public class UndoRefactoringProposalRename extends UndoRefactoringProposal{

	public UndoRefactoringProposalRename(JavaUndoRefactoring u) {
		super(u);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDisplayString() {
		return "Undo rename refactoring.";
	}

	@Override
	protected String getImageFileName() {
		return "undo_rename.jpg";
	}

}
