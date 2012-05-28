package JavaRefactoringAPI.extract.method;

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
import extract.method.ASTChangeEM;

import util.ICompilationUnitUtil;
import util.UIUtil;

import ASTree.CUHistory.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;

public class JavaRefactoringExtractMethodChange extends JavaRefactoringExtractMethodBase {

	@SuppressWarnings("restriction")
	
	ASTChangeEM information;
	
	public JavaRefactoringExtractMethodChange(ICompilationUnit u, int l ,ASTChangeEM info) throws Exception{
		super(u, l);
		information = info;	
	}
	
	protected CompilationUnitHistoryRecord getNonrefactoringChangeStart()
	{
		return information.getNewCompilationUnitRecord();
	}
	
	@SuppressWarnings("restriction")
	@Override
	public void performRefactoring(IProgressMonitor pm) throws Exception{
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
	
	public ASTChangeEM getExtractMethodChangeInformation()
	{
		return information;
	}
	

	


	@Override
	protected String getSourceAfterRefactoring() {
		return  information.getNewCompilationUnitRecord().getAllHistory().getMostRecentRecord().getSourceCode();
	}
	@Override
	protected String getSourceAfterRecovery() {
		return information.getOldCompilationUnitRecord().getSourceCode();
	}

	@Override
	protected CompilationUnitHistoryRecord getLatestRecord() throws Exception {
		return information.getNewCompilationUnitRecord().getAllHistory().getMostRecentRecord();
	}


}
