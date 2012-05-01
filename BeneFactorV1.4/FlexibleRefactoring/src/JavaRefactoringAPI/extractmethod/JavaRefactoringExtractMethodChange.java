package JavaRefactoringAPI.extractmethod;

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

import utitilies.UIUtil;

import ASTree.CompilationUnitHistoryRecord;
import ASTree.CompilationUnitManipulationMethod;
import ExtractMethod.ASTExtractMethodChangeInformation;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;

public class JavaRefactoringExtractMethodChange extends JavaRefactoringExtractMethodBase {

	@SuppressWarnings("restriction")
	
	ASTExtractMethodChangeInformation information;
	CompilationUnitHistoryRecord non_refactoring_change_end;
	
	
	
	public JavaRefactoringExtractMethodChange(ICompilationUnit u, int l,IMarker m,ASTExtractMethodChangeInformation info) throws Exception{
		super(u, l, m);
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
	
		while(endR.getSourceCode().equals(source_after_refactoring) 
				|| endR.getSourceCode().equals(source_after_recovering))
			endR = endR.getPreviousRecord();
		
		return endR;
	}
	
	@SuppressWarnings("restriction")
	@Override
	public void performRefactoring(IProgressMonitor pm) {
		int[] index;
		index = information.getSelectionStartAndEnd(this.getICompilationUnit());
		try {
			int selectionStart = index[0];
			int selectionLength = index[1] - index[0] + 1;
			JavaRefactoringExtractMethodUtil.performEclipseRefactoring
			(this.getICompilationUnit(), selectionStart, selectionLength, this.getModifier(), this.getMethodName(), pm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



	@Override
	protected void performCodeRecovery(IProgressMonitor monitor) {
		information.recoverICompilationUnitToOldRecord(monitor);
		
	}



	
	public ASTExtractMethodChangeInformation getExtractMethodChangeInformation()
	{
		return information;
	}
	

	public JavaRefactoringExtractMethodBase moveExtractMethodRefactoring(IMarker marker, int l) throws Exception
	{
		JavaRefactoringExtractMethodChange refactoring = 
				new JavaRefactoringExtractMethodChange(getICompilationUnit(), l, marker, getExtractMethodChangeInformation());
		refactoring.setModifier(this.getModifier());
		refactoring.setMethodName(this.getMethodName());
		return refactoring; 
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
		JavaRefactoringExtractMethodUtil.prepareLinkedEdition(this.getICompilationUnit(), this.getMethodName());
		
		MonitorUiPlugin.getDefault().notifyInteractionObserved(InteractionEvent.makeCommand(event_id + ".ExtractMethod", "extract method"));
	}
	
	private void redoUnrefactoringChanges(CompilationUnitHistoryRecord startRecord, CompilationUnitHistoryRecord endRecord) throws Exception
	{
		String source;
		source = this.getICompilationUnit().getSource();
		LinkedList<Patch> patches = JavaSourceDiff.getPatches(startRecord.getSourceCode(), endRecord.getSourceCode());
		source = JavaSourceDiff.applyPatches(source, patches);
		CompilationUnitManipulationMethod.UpdateICompilationUnit(this.getICompilationUnit(), source, new NullProgressMonitor());
	}
	
	

}
