package userinterface;



import java.io.File;

import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import JavaRefactoringAPI.JavaRefactoringType;


public abstract class RefactoringProposal implements IJavaCompletionProposal{

	@Override
	public abstract void apply(IDocument document);
	@Override
	public Point getSelection(IDocument document){return null;};
	@Override
	public String getAdditionalProposalInfo(){return null;};
	@Override
	public abstract String getDisplayString();
	@Override
	public abstract Image getImage();
	@Override
	public IContextInformation getContextInformation() {return null;};
	@Override
	public int getRelevance(){return Integer.MAX_VALUE;};
	
	public static RefactoringProposal getRefactoringProposalByType(int type)
	{
		switch(type)
		{
		case JavaRefactoringType.RENAME:
			return new RefactoringProposalRename();
		case JavaRefactoringType.EXTRACT_METHOD:
			return new RefactoringProposalExtractMethod();
		default:
			return null;
		}
	}
}
