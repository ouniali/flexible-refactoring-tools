package userinterface;

import java.io.File;

import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import flexiblerefactoring.BeneFactor;

public class MockProposal implements IJavaCompletionProposal{

	@Override
	public void apply(IDocument document) {}

	@Override
	public Point getSelection(IDocument document) {
		return null;}

	@Override
	public String getAdditionalProposalInfo() {
		return null;
	}

	@Override
	public String getDisplayString() {
		return "Finish extract method";
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		String path = BeneFactor.getIconPath("refactoring.png");
		if(new File(path).exists())
		{
			Display display = PlatformUI.getWorkbench().getDisplay();
			Image icon = new Image(display, path);
			return icon;
		}
		else
			return null;
	}

	@Override
	public IContextInformation getContextInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRelevance() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

}
