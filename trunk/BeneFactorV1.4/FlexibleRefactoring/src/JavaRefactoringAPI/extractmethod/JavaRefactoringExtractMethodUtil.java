package JavaRefactoringAPI.extractmethod;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
import org.eclipse.jdt.internal.ui.javaeditor.EditorHighlightingSynchronizer;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.link.LinkedModeModel;
import org.eclipse.jface.text.link.LinkedModeUI;
import org.eclipse.jface.text.link.LinkedPosition;
import org.eclipse.jface.text.link.LinkedPositionGroup;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ui.texteditor.link.EditorLinkedModeUI;

import utitilies.UserInterfaceUtilities;

public class JavaRefactoringExtractMethodUtil {
	
	
	@SuppressWarnings("restriction")
	public static void prepareLinkedEdition(ICompilationUnit unit, String methodName)
	{
		try {
			JavaEditor editor = UserInterfaceUtilities.getActiveJavaEditor();
			if(editor == null)
				return;
			IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput()); 
			LinkedModeModel model = new LinkedModeModel();
			LinkedPositionGroup method_name_group = new LinkedPositionGroup();
			String source = unit.getSource();
			
			int start = source.indexOf(methodName, 0);
			for(; start != -1; start = source.indexOf(methodName + methodName.length() ))
				method_name_group.addPosition(new LinkedPosition(document, start, methodName.length(), 0));
			model.addGroup(method_name_group);
			model.forceInstall();
			model.addLinkingListener(new EditorHighlightingSynchronizer(editor));
			
			ITextViewer viewer = editor.getViewer();
			LinkedModeUI ui= new EditorLinkedModeUI(model, viewer);		
			ui.enter();
			LinkedPosition p = method_name_group.getPositions()[0];
			viewer.setSelectedRange(p.offset, p.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static String getExtractedMethodName(ICompilationUnit unit) {
		int index = 0;
		if (index == 0)
			return "extractedMethod";
		else
			return "extractedMethod" + Integer.toString(index);
	}
	
	@SuppressWarnings("restriction")
	public static void performEclipseRefactoring(ICompilationUnit unit, int start, 
			int length, int modifier, String methodName, IProgressMonitor pm) throws Exception
	{
		SubMonitor monitor = SubMonitor.convert(pm,"Performing Extract Method Refactoring",4);
		
		ExtractMethodRefactoring refactoring;
		RefactoringStatus iniStatus;
		RefactoringStatus finStatus;

		refactoring = new ExtractMethodRefactoring(unit, start, length);
		refactoring.setReplaceDuplicates(false);
		refactoring.setVisibility(modifier);
		refactoring.setMethodName(methodName);
		
		iniStatus = refactoring.checkInitialConditions(monitor.newChild(1));
		if (!iniStatus.isOK())
			return;
		finStatus = refactoring.checkFinalConditions(monitor.newChild(1));
		if (!finStatus.isOK())
			return;
		Change change = refactoring.createChange(monitor.newChild(1));
		Change undo = change.perform(monitor.newChild(1));
		monitor.done();
	}

}
