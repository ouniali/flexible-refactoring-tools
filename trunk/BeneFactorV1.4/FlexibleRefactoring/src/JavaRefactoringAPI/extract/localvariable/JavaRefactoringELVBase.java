package JavaRefactoringAPI.extract.localvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

import extract.localvariable.ELVActivity;

import ASTree.CompilationUnitHistoryRecord;
import ASTree.CompilationUnitManipulationMethod;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;
import org.eclipse.jdt.internal.corext.refactoring.code.*;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

public abstract class JavaRefactoringELVBase extends JavaRefactoring{

	ELVActivity activity;
	String temp_name = "temp";
	CompilationUnitHistoryRecord non_refactoring_end;
	
	public JavaRefactoringELVBase(ICompilationUnit u, int l, ELVActivity a)
			throws Exception {
		super(u, l);
		activity = a;
	}
	
	protected final String getTempName() {
		return temp_name;
	}

	public void setTempName(String t) {
		temp_name = t;
	}
	
	public final ELVActivity getActivity()
	{
		return activity;
	}
	
	@SuppressWarnings("restriction")
	@Override
	final protected void performRefactoring(IProgressMonitor pm) throws Exception {
		ICompilationUnit unit = getICompilationUnit();
		unit.becomeWorkingCopy(null);
		NullProgressMonitor monitor = new NullProgressMonitor();
		int start = activity.getRecord().getSeletectedRegion()[0];
		int length = activity.getRecord().getSeletectedRegion()[1] 
				- activity.getRecord().getSeletectedRegion()[0] + 1;
		ExtractTempRefactoring refactoring = new ExtractTempRefactoring(unit, start, length);
		refactoring.setTempName(getTempName());
		RefactoringStatus status = refactoring.checkAllConditions(monitor);
		if(status.isOK())
			refactoring.createChange(monitor).perform(monitor);
		unit.commitWorkingCopy(true, monitor);
		unit.discardWorkingCopy();
	}

	@Override
	protected final void performCodeRecovery(IProgressMonitor pm) throws Exception {
		ICompilationUnit unit = this.getICompilationUnit();
		String code = activity.getRecord().getSourceCode();
		CompilationUnitManipulationMethod.UpdateICompilationUnitWithoutCommit(unit, code, null);
	}

	@Override
	public final int getRefactoringType() {
		return JavaRefactoringType.EXTRACT_LOCAL_VARIABLE;
	}

	@Override
	public void preProcess() throws Exception {
		
	}

	@Override
	final public void postProcess() throws Exception 
	{
		CompilationUnitHistoryRecord start = activity.getRecord();
		CompilationUnitHistoryRecord end = getNonRefactoringChangeEnd();
		redoUnrefactoringChanges(start, end);
	}
	
	public void setNonRefactoringChangeEnd(CompilationUnitHistoryRecord end)
	{
		non_refactoring_end = end;
	}
	
	public CompilationUnitHistoryRecord getNonRefactoringChangeEnd()
	{
		if(non_refactoring_end != null)
			return non_refactoring_end;
		else
			return activity.getRecord().getAllHistory().getMostRecentRecord();
	}
	
	

}
