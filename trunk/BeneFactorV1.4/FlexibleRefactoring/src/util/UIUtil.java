package util;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.ui.IWorkingCopyManager;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.mylyn.internal.monitor.ui.MonitorUiPlugin;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;

import StyledTextHelper.StyledTextGetBound;
import StyledTextHelper.StyledTextGetLineHeight;
import StyledTextHelper.StyledTextGetPoint;
import StyledTextHelper.StyledTextOffsetAndLine;



public class UIUtil {

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
	
	static public JavaEditor getJavaEditor(ICompilationUnit unit)
	{
		
		IWorkbenchPage[] pages = getWorkbenchPages();
		for(IWorkbenchPage page : pages)
		{
			IEditorPart part = page.getActiveEditor();
			if(part instanceof CompilationUnitEditor)
			{
				CompilationUnitEditor editor = (CompilationUnitEditor) part;
				ICompilationUnit editor_unit= getConnectedICompilationUnit(editor);
				if(editor_unit.getPath().toOSString().equals(unit.getPath().toOSString()))
					return editor;
			}
		}	
		
		return null;
	}
	
	
	static public ICompilationUnit getConnectedICompilationUnit(CompilationUnitEditor editor)
	{
		IWorkingCopyManager manager= JavaPlugin.getDefault().getWorkingCopyManager();
		ICompilationUnit unit= manager.getWorkingCopy(editor.getEditorInput());
		return unit;
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
	
	/**
	 * @author Xi Ge
	 *	call reconcile explicitly
	 * 
	 */
	
	static public void reconcileActiveEditor()
	{
		try {
			JavaEditor editor = getActiveJavaEditor();
			ICompilationUnit unit = getConnectedICompilationUnit ((CompilationUnitEditor)editor);
			unit.reconcile(AST.JLS3, true, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * @author Xi Ge
	 * Get the selected range in the active JavaEditor.
	 * @throws Exception 
	 */
	static public int[] getSelectedRangeInActiveEditor() throws Exception
	{	
		Runnable temp = new Runnable(){
			int start;
			int end;
			public void run() {
				JavaEditor editor = getActiveJavaEditor();
				ITextSelection ts = null;
				ts = (ITextSelection) editor.getSelectionProvider().getSelection();
				if(ts == null)
				{
					start = 0;
					end = 0;
				}
				else
				{
					start = ts.getOffset();
					end = ts.getOffset() + ts.getLength() - 1;
				//	System.out.println(start + " " + end);
				}
					
			}
		};
		Display.getDefault().syncExec(temp);
		return new int[]{temp.getClass().getDeclaredField("start").getInt(temp),
				temp.getClass().getDeclaredField("end").getInt(temp)};
	}
	
	
	
	static public IWorkbenchPage getEditorWorkbenchPage()
	{
		
		IWorkbenchPage[] pages = getWorkbenchPages();
		for(IWorkbenchPage page : pages)
		{
			IEditorPart part = page.getActiveEditor();
			if(part instanceof JavaEditor)
				return page;
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
	
	
	public static int getLineNumberByOffset(int offset, JavaEditor editor)
	{
		StyledTextOffsetAndLine stHelper = new StyledTextOffsetAndLine(offset, true, editor);
		Display.getDefault().syncExec(stHelper);
		return stHelper.getLineNumber();
	}
	
	public static int getOffsetByLineNumber(int line, JavaEditor editor)
	{
		StyledTextOffsetAndLine stHelper = new StyledTextOffsetAndLine(line, false, editor);
		Display.getDefault().syncExec(stHelper);
		return stHelper.getOffset();
	}
	

	
	public static void closeJavaEditor(final JavaEditor editor)
	{
		Display.getDefault().syncExec(new Runnable(){
			public void run()
			{
				editor.getEditorSite().getPage().closeEditor(editor, false);
			}
		});
		
	}
	
	public static JavaEditor openJavaEditor(final ICompilationUnit unit) throws Exception
	{
		Runnable open = new Runnable() {			
			public void run() {
				try {
					IWorkbenchPage page = getEditorWorkbenchPage();
					IEditorPart javaEditor;
					javaEditor = JavaUI.openInEditor(unit);
					page.activate(javaEditor);	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		Display.getDefault().syncExec(open);
		return getActiveJavaEditor();
	}
	
	
	
	public static void freezeEditor(JavaEditor activeJavaEditor) {
		// TODO Auto-generated method stub
		
	}

	public static void wakeUpEditor(JavaEditor activeJavaEditor) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
}
