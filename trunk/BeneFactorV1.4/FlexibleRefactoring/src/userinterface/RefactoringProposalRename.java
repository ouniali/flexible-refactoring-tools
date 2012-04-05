package userinterface;

import java.io.File;

import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import flexiblerefactoring.BeneFactor;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringRename;
import JavaRefactoringAPI.JavaRefactoringRenameDiff;

public class RefactoringProposalRename extends RefactoringProposal{
	
	public RefactoringProposalRename(JavaRefactoring ref)
	{
		super(ref);
	}
	public String getDisplayString() {
		// TODO Auto-generated method stub
		JavaRefactoring ref = this.refactoring;
		String detail;
		if(ref instanceof JavaRefactoringRename)
		{
			JavaRefactoringRename ref1 = (JavaRefactoringRename) ref;
			detail = ": \"" + ref1.getOldName() +"\" to \"" + ref1.getNewName()+"\"";
		}
		else if (ref instanceof JavaRefactoringRenameDiff)
		{
			JavaRefactoringRenameDiff ref2 = (JavaRefactoringRenameDiff) ref;
			detail = ": \"" + ref2.getOldName() +"\" to \"" + ref2.getNewName()+ "\"";
		}
		else
		{
			detail ="";
		}
		return "Finish rename refactoring" + detail;
	}

	@Override
	protected String getImageFileName() {
		// TODO Auto-generated method stub
		return "rename.jpg";
	}
}
