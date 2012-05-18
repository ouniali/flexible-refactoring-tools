package userinterface;

import JavaRefactoringAPI.JavaRefactoring;

public class RefactoringProposalExtractLocalVariable extends
		RefactoringProposal {

	public RefactoringProposalExtractLocalVariable(JavaRefactoring ref) 
	{
		super(ref);
	}

	@Override
	public String getDisplayString() 
	{
		return "Finish Extract Local Variable";
	}

	@Override
	protected String getImageFileName() {
		return "rename.jpg";
	}

}
