package StyledTextHelper;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPart;

public class StyledTextHelper implements Runnable{
	
	
	protected StyledText getStyledTextHelper(IWorkbenchPart part) {
	      ITextViewer viewer = (ITextViewer) part.getAdapter(ITextViewer.class);
	      StyledText textWidget = null;
	      if (viewer == null) {
	        Control control = (Control) part.getAdapter(Control.class);
	        if (control instanceof StyledText) {
	          textWidget = (StyledText) control;
	        }
	      } else {
	        textWidget = viewer.getTextWidget();
	      }
	      return textWidget;
	    }

	@Override
	public void run() {
		
		
	}
}
