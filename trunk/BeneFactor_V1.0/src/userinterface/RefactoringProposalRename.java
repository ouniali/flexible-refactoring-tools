package userinterface;

import java.io.File;

import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import flexiblerefactoring.BeneFactor;
import JavaRefactoringAPI.JavaRefactoring;

public class RefactoringProposalRename extends RefactoringProposal{
	
	public RefactoringProposalRename(JavaRefactoring ref)
	{
		super(ref);
	}
	public String getDisplayString() {
		// TODO Auto-generated method stub
		return "Finish rename refactoring";
	}

	@Override
	protected String getImageFileName() {
		// TODO Auto-generated method stub
		return "rename.jpg";
	}
}
