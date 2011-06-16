package userinterface;



import java.io.File;

import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;


public class CompletionRefactoringProposal implements IJavaCompletionProposal{

	@Override
	public void apply(IDocument document) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point getSelection(IDocument document) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAdditionalProposalInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayString() {
		// TODO Auto-generated method stub
		return "Finishing Rename Refactoring";
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		Display display = PlatformUI.getWorkbench().getDisplay();
		Image icon = new Image(display, "C:" +File.separator+ "refactoring.png");
		return icon;
	}

	@Override
	public IContextInformation getContextInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRelevance() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE-1;
	}

}
