package userinterface;

import JavaRefactoringAPI.JavaRefactoring;

public class RefactoringProposalMoveStaticMember extends RefactoringProposal{

	public RefactoringProposalMoveStaticMember(JavaRefactoring ref) {
		super(ref);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDisplayString() {
		// TODO Auto-generated method stub
		return "Finish move static member";
	}

	@Override
	protected String getImageFileName() {
		// TODO Auto-generated method stub
		return "rename.jpg";
	}

}
