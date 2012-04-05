package JavaRefactoringAPI;

import java.util.ArrayList;
import java.util.LinkedList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Modifier;
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
import org.eclipse.mylyn.internal.monitor.ui.MonitorUiPlugin;
import org.eclipse.mylyn.monitor.core.InteractionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.link.EditorLinkedModeUI;

import compare.JavaSourceDiff;
import compare.SourceDiff;
import compare.diff_match_patch.Patch;

import utitilies.UserInterfaceUtilities;

import ASTree.CompilationUnitHistoryRecord;
import ASTree.CompilationUnitManipulationMethod;
import ExtractMethod.ASTExtractMethodChangeInformation;

public class JavaRefactoringExtractMethod extends JavaRefactoring {

	@SuppressWarnings("restriction")
	ExtractMethodRefactoring refactoring;
	ASTExtractMethodChangeInformation information;
	CompilationUnitHistoryRecord non_refactoring_change_end;
	static int extractedMethodCount = 0;
	int modifier;
	String returnType;
	String methodName;
	
	
	
	public JavaRefactoringExtractMethod(ICompilationUnit u, int l,IMarker m,ASTExtractMethodChangeInformation info) {
		super(u, l, m);
		modifier = Modifier.PRIVATE;
		methodName = getExtractedMethodName();
		information = info;
		
	}

	public void setNonrefactoringChangeEnd(CompilationUnitHistoryRecord r)
	{
		non_refactoring_change_end = r;
	}
	
	private CompilationUnitHistoryRecord getNonrefactoringChangeStart()
	{
		return information.getNewCompilationUnitRecord();
	}
	
	private CompilationUnitHistoryRecord getNonRefactoringChangeEnd()
	{
		if(non_refactoring_change_end != null)
			return non_refactoring_change_end;
		CompilationUnitHistoryRecord latestR = information.getNewCompilationUnitRecord().getAllHistory().getMostRecentRecord();
		String source_after_refactoring = latestR.getSourceCode();
		String source_after_recovering = information.getOldCompilationUnitRecord().getSourceCode();
		CompilationUnitHistoryRecord endR = latestR;
		
		//traceback to before recovering code
		while(endR.getSourceCode().equals(source_after_refactoring) 
				|| endR.getSourceCode().equals(source_after_recovering))
			endR = endR.getPreviousRecord();
		
		return endR;
	}
	
	@SuppressWarnings("restriction")
	@Override
	public void performRefactoring(IProgressMonitor pm) {
		// TODO Auto-generated method stub

		SubMonitor monitor = SubMonitor.convert(pm,"Performing Extract Method Refactoring",4);
		
		RefactoringStatus iniStatus;
		RefactoringStatus finStatus;
		int[] index;

		index = information.getSelectionStartAndEnd(this.getICompilationUnit());

		try {
			int selectionStart = index[0];
			int selectionLength = index[1] - index[0] + 1;
			refactoring = new ExtractMethodRefactoring(this.getICompilationUnit(), selectionStart,
					selectionLength);
			refactoring.setReplaceDuplicates(true);
			refactoring.setVisibility(modifier);
			refactoring.setMethodName(methodName);
			// wait for the underlying resource to be ready
			//	Thread.sleep(WAIT_TIME);
			
			iniStatus = refactoring.checkInitialConditions(monitor.newChild(1));
			if (!iniStatus.isOK())
				return;
			finStatus = refactoring.checkFinalConditions(monitor.newChild(1));
			if (!finStatus.isOK())
				return;
			Change change = refactoring.createChange(monitor.newChild(1));
			
			Change undo = change.perform(monitor.newChild(1));
			this.setUndo(undo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		monitor.done();
	}


	String getExtractedMethodName() {
		int index = extractedMethodCount;
		extractedMethodCount ++;
		if (index == 0)
			return "extractedMethod";
		else
			return "extractedMethod" + Integer.toString(index);
	}

	@Override
	protected void performCodeRecovery(IProgressMonitor monitor) {
		information.recoverICompilationUnitToOldRecord(monitor);
		
	}


	@Override
	public int getRefactoringType() {
		return JavaRefactoringType.EXTRACT_METHOD;
	}
	
	public ASTExtractMethodChangeInformation getExtractMethodChangeInformation()
	{
		return information;
	}
	
	public void setMethodModifier(int m)
	{
		modifier = m;
	}
	
	public void setMethodName(String m)
	{
		methodName = m;
	}
	
	public JavaRefactoringExtractMethod moveExtractMethodRefactoring(IMarker marker, int l)
	{
		JavaRefactoringExtractMethod refactoring = new JavaRefactoringExtractMethod(getICompilationUnit(), l, marker, getExtractMethodChangeInformation());
		refactoring.setMethodModifier(modifier);
		refactoring.setMethodName(methodName);
		return refactoring; 
	}
	
	@SuppressWarnings("restriction")
	private void prepareLinkedEdition()
	{
		try {
			JavaEditor editor = UserInterfaceUtilities.getActiveJavaEditor();
			if(editor == null)
				return;
			IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput()); 
			LinkedModeModel model = new LinkedModeModel();
			LinkedPositionGroup method_name_group = new LinkedPositionGroup();
			String source = this.getICompilationUnit().getSource();
			
			int start = source.indexOf(this.methodName, 0);
			for(; start != -1; start = source.indexOf(this.methodName, start + this.methodName.length() ))
				method_name_group.addPosition(new LinkedPosition(document, start, this.methodName.length(), 0));
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


	@Override
	public void postProcess() {
		
		CompilationUnitHistoryRecord startR = this.getNonrefactoringChangeStart();
		CompilationUnitHistoryRecord endR = this.getNonRefactoringChangeEnd();
		
		
		try{
			redoUnrefactoringChanges(startR, endR);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		Display.getDefault().asyncExec(new Runnable() {
		       public void run() {prepareLinkedEdition();}
		}
		);
		
		MonitorUiPlugin.getDefault().notifyInteractionObserved(InteractionEvent.makeCommand(event_id, "extract method"));
	}
	
	private void redoUnrefactoringChanges(CompilationUnitHistoryRecord startRecord, CompilationUnitHistoryRecord endRecord) throws Exception
	{
		String source;
		source = this.getICompilationUnit().getSource();
		LinkedList<Patch> patches = JavaSourceDiff.getPatches(startRecord.getSourceCode(), endRecord.getSourceCode());
		source = JavaSourceDiff.applyPatches(source, patches);
		CompilationUnitManipulationMethod.UpdateICompilationUnit(this.getICompilationUnit(), source, new NullProgressMonitor());
	}

	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
		
	}




	
	
	
	

}
