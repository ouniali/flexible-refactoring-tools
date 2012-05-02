package JavaRefactoringAPI.extractmethod;

import java.lang.reflect.Modifier;
import java.util.LinkedList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.mylyn.internal.monitor.ui.MonitorUiPlugin;
import org.eclipse.mylyn.monitor.core.InteractionEvent;

import compare.JavaSourceDiff;
import compare.diff_match_patch.Patch;

import ASTree.CompilationUnitHistoryRecord;
import ASTree.CompilationUnitManipulationMethod;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;

public abstract class JavaRefactoringExtractMethodBase extends JavaRefactoring{

	String methodName;
	int modifier;
	CompilationUnitHistoryRecord non_refactoring_change_end;
	
	public final void setMethodName(String m)
	{
		methodName = m;
	}
	
	public final String getMethodName()
	{
		return methodName;
	}
	
	public final void setModifier(int m)
	{
		modifier = m;
	}
	
	public final int getModifier()
	{
		return modifier;
	}
	

	protected abstract CompilationUnitHistoryRecord getNonrefactoringChangeStart();
	
	public void setNonrefactoringChangeEnd(CompilationUnitHistoryRecord r)
	{
		non_refactoring_change_end = r;
	}
	
	final protected CompilationUnitHistoryRecord getNonRefactoringChangeEnd()	
	{
		if(non_refactoring_change_end != null)
			return non_refactoring_change_end;
		CompilationUnitHistoryRecord endR = getRecordAfterRefactoring();
		while(endR.getSourceCode().equals(getRecordAfterRefactoring().getSourceCode()) 
				|| endR.getSourceCode().equals(getRecordAfterRecovery().getSourceCode()))
			endR = endR.getPreviousRecord();
		return endR;
	}
	
	
	abstract protected CompilationUnitHistoryRecord getRecordAfterRefactoring();
	abstract protected CompilationUnitHistoryRecord getRecordAfterRecovery();
	
	
	
	
	
	public JavaRefactoringExtractMethodBase(ICompilationUnit u, int l, IMarker m)
			throws Exception {
		super(u, l, m);
		setMethodName(JavaRefactoringExtractMethodUtil.getExtractedMethodName(this.getICompilationUnit()));
		modifier = Modifier.PRIVATE;
	}

	@Override
	protected abstract void performRefactoring(IProgressMonitor pm) throws Exception;

	@Override
	protected abstract void performCodeRecovery(IProgressMonitor pm) throws Exception;

	@Override
	public final int getRefactoringType() {
		return JavaRefactoringType.EXTRACT_METHOD;
	}

	@Override
	public void preProcess() throws Exception
	{
		
	}

	@Override
	public void postProcess() throws Exception
	{
		CompilationUnitHistoryRecord startR = getNonrefactoringChangeStart();
		CompilationUnitHistoryRecord endR = getNonRefactoringChangeEnd();
		redoUnrefactoringChanges(startR, endR);
		JavaRefactoringExtractMethodUtil.prepareLinkedEdition(this.getICompilationUnit(), this.getMethodName());	
		MonitorUiPlugin.getDefault().notifyInteractionObserved(InteractionEvent.makeCommand(event_id + ".ExtractMethod", "extract method"));
	}
	
	private void redoUnrefactoringChanges(CompilationUnitHistoryRecord startRecord, CompilationUnitHistoryRecord endRecord) throws Exception
	{
		String source;
		source = getICompilationUnit().getSource();
		LinkedList<Patch> patches = JavaSourceDiff.getPatches(startRecord.getSourceCode(), endRecord.getSourceCode());
		source = JavaSourceDiff.applyPatches(source, patches);
		CompilationUnitManipulationMethod.UpdateICompilationUnit(this.getICompilationUnit(), source, new NullProgressMonitor());
	}
	
	public abstract JavaRefactoringExtractMethodBase moveExtractMethodRefactoring(IMarker marker, int l) throws Exception;
	
}
