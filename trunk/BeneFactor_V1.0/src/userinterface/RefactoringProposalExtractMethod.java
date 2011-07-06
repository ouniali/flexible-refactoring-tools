package userinterface;

import java.io.File;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import JavaRefactoringAPI.JavaRefactoring;

public class RefactoringProposalExtractMethod extends RefactoringProposal{

	public RefactoringProposalExtractMethod(JavaRefactoring ref)
	{
		super(ref);
	}

	@Override
	public String getDisplayString() {
		// TODO Auto-generated method stub
		return "Finish extract method";
	}

	@Override
	protected String getImagePath() {
		// TODO Auto-generated method stub
		return "C:" +File.separator+ "refactoring.png";
	}

}
