package animation.folding;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.source.projection.ProjectionViewer;

import util.UIUtil;

public class FoldingTester implements Runnable{

	FoldingElementChangeListener listener;
	JavaEditor editor;
	
	public FoldingTester (FoldingElementChangeListener l)
	{
		listener = l;
		editor = (JavaEditor)l.getEditor();
	}
	@Override
	public void run() {
		
		int off;
		int hidedlength = 0;
		ProjectionViewer viewer = listener.getViewer();
		for(int line = 2; line <15; line ++)
		{
			off = UIUtil.getOffsetByLineNumber(2, editor) ;
			off = viewer.widgetOffset2ModelOffset(off);
			listener.setHidingArea(0, off);
			System.out.println(line + " " + off);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
