package utitilies;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;

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
	
	static public void freezeEditor(JavaEditor editor)
	{

	}
}
