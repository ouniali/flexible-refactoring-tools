package userinterface;

import JavaRefactoringAPI.JavaUndoRefactoring;

public class UndoRefactoringProposalExtractMethod extends UndoRefactoringProposal{

	public UndoRefactoringProposalExtractMethod(JavaUndoRefactoring u) {
		super(u);
	}

	@Override
	public String getDisplayString() {
		return "Undo extract method.";
	}

	@Override
	protected String getImagePath() {
		return null;
	}

}
