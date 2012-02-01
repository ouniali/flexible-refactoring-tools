package utitilies;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.mylyn.internal.monitor.ui.MonitorUiPlugin;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;

public class UserInterfaceUtilities {

	static public IWorkbenchWindow[] getWorkbenchWindows()
	{
		return PlatformUI.getWorkbench().getWorkbenchWindows();
	}
	
	static public IWorkbenchPage[] getWorkbenchPages()
	{
		IWorkbenchWindow[] windows = getWorkbenchWindows();
		ArrayList<IWorkbenchPage> pages = new ArrayList<IWorkbenchPage>();
		for(IWorkbenchWindow win : windows)
		{
			for (IWorkbenchPage page : win.getPages())
				pages.add(page);
		}
		IWorkbenchPage[] result = new IWorkbenchPage[pages.size()];
		return pages.toArray(result);
	}
	
	@SuppressWarnings("restriction")
	static public JavaEditor getActiveJavaEditor()
	{
		
		IWorkbenchPage[] pages = getWorkbenchPages();
		for(IWorkbenchPage page : pages)
		{
			IEditorPart part = page.getActiveEditor();
			if(part instanceof JavaEditor)
				return (JavaEditor) part;
		}
		return null;
	}
	
	public static Point getEditorPointInDisplay(int offset, JavaEditor editor)
	{
		StyledTextHelper stHelper = new StyledTextHelper(editor, offset);
		Display.getDefault().asyncExec(stHelper);
		Point p = stHelper.getPoint();
		return p;
	}
	
	public static int getEditorLineHeight(int offset, JavaEditor editor)
	{
		StyledTextHelper stHelper = new StyledTextHelper(editor, offset);
		Display.getDefault().asyncExec(stHelper);
		return stHelper.line_height;
	}
	
	private static class StyledTextHelper implements Runnable
	{
		JavaEditor editor;
		Point Position;
		int offset;
		int line_height;
		
		boolean know_offset;
		
		public StyledTextHelper(JavaEditor e, int o)
		{
			editor = e;
			offset = o;
			know_offset = true;
		}
		
		public StyledTextHelper(JavaEditor e, Point p)
		{
			editor = e;
			Position = p;
			know_offset = false;
		}
		
		public Point getPoint()
		{
			return Position;
		}
		
		public int getLineHeight()
		{
			return line_height;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			StyledText st = getStyledText(editor);
			if(know_offset)
				Position = st.toDisplay(st.getLocationAtOffset(offset));
			else
				offset = st.getOffsetAtLocation(st.toControl(Position));
			line_height = st.getLineHeight(offset);
		}
		
		private StyledText getStyledText(IWorkbenchPart part) {
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
		
	}
	
	

	
	static public void freezeEditor(final JavaEditor editor)
	{
		Display.getDefault().asyncExec(new Runnable() {
			public void run() 
			{
				
			}
		});

	}
	
	static public void wakeUpEditor(final JavaEditor editor)
	{
		Display.getDefault().asyncExec(new Runnable() {
			public void run() 
			{
				
			}
		});
		
	}
	
}
