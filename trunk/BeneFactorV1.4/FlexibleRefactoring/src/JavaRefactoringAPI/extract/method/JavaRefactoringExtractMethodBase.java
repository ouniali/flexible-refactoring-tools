	package JavaRefactoringAPI.extract.method;

import java.lang.reflect.Modifier;
import java.util.LinkedList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.mylyn.internal.monitor.ui.MonitorUiPlugin;
import org.eclipse.mylyn.monitor.core.InteractionEvent;

import util.ICompilationUnitUtil;

import compare.JavaSourceDiff;
import compare.diff_match_patch.Patch;

import ASTree.CUHistory.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;

public abstract class JavaRefactoringExtractMethodBase extends JavaRefactoring{

	String methodName;
	int modifier;
	CompilationUnitHistoryRecord non_refactoring_change_end;
	private static boolean PERFORM_POST_PROCESS = false;
	
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
	
	public void setNonrefactoringChangeEnd(CompilationUnitHistoryRecord r)
	{
		non_refactoring_change_end = r;
	}
	
	final protected CompilationUnitHistoryRecord getNonRefactoringChangeEnd() throws Exception	
	{
		if(non_refactoring_change_end != null)
			return non_refactoring_change_end;
		CompilationUnitHistoryRecord endR = getLatestRecord();
		CompilationUnitHistoryRecord after = null;
		
		while( 	endR != null && endR.isLaterThan(getNonrefactoringChangeStart()) &&
				(endR.getSourceCode().equals(getSourceAfterRefactoring())
				|| endR.getSourceCode().equals(getSourceAfterRecovery())))
		{	
			after = endR; 
			endR = endR.getPreviousRecord();
		}
		if(endR == null)
			return after;
		else 
			return endR;
	}
	
	abstract protected CompilationUnitHistoryRecord getNonrefactoringChangeStart();
	abstract protected String getSourceAfterRefactoring() throws Exception;
	abstract protected String getSourceAfterRecovery() throws Exception;
	abstract protected CompilationUnitHistoryRecord getLatestRecord() throws Exception;
	@Override
	protected abstract void performRefactoring(IProgressMonitor pm) throws Exception;
	@Override
	protected abstract void performCodeRecovery(IProgressMonitor pm) throws Exception;

	
	
	public JavaRefactoringExtractMethodBase(ICompilationUnit u, int l)
			throws Exception {
		super(u, l);
		setMethodName(JavaRefactoringExtractMethodUtil.getExtractedMethodName(this.getICompilationUnit()));
		modifier = Modifier.PRIVATE;
	}



	
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
		if(JavaRefactoringExtractMethodBase.PERFORM_POST_PROCESS){
			CompilationUnitHistoryRecord startR = getNonrefactoringChangeStart();
			CompilationUnitHistoryRecord endR = getNonRefactoringChangeEnd();
			redoUnrefactoringChanges(startR, endR);
			MonitorUiPlugin.getDefault().notifyInteractionObserved(
				InteractionEvent.makeCommand(event_id + ".ExtractMethod", "extract method"));
		}
	}
	

	
	
}
 	