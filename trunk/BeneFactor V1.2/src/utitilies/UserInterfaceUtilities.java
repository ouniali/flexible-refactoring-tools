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
import org.eclipse.swt.graphics.Rectangle;
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

import StyledTextHelper.StyledTextGetBound;
import StyledTextHelper.StyledTextGetLineHeight;
import StyledTextHelper.StyledTextGetPoint;

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
		StyledTextGetPoint stHelper = new StyledTextGetPoint(offset, editor);
		Display.getDefault().syncExec(stHelper);
		return stHelper.getPositionToDisplay();
	}
	
	public static int getEditorLineHeight(int offset, JavaEditor editor)
	{
		StyledTextGetLineHeight stHelper = new StyledTextGetLineHeight(offset, editor);
		Display.getDefault().syncExec(stHelper);
		return stHelper.getLineHeight();
	}
	
	public static Rectangle getTextBounds(int start, int end, JavaEditor editor)
	{
		StyledTextGetBound stHelper = new StyledTextGetBound(start, end, editor);
		Display.getDefault().syncExec(stHelper);
		return stHelper.getTextBound();
	}

	public static void freezeEditor(JavaEditor activeJavaEditor) {
		// TODO Auto-generated method stub
		
	}

	public static void wakeUpEditor(JavaEditor activeJavaEditor) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
}
