package userinterface;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.UIPlugin;

import flexiblerefactoring.BeneFactor;

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
	protected String getImageFileName() {
		return "rename.jpg";
	}

}
