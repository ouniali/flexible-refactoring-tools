package JavaRefactoringAPI.extractlocalvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

import extractlocalvariable.ExtractLocalVariableActivity;

import ASTree.CompilationUnitHistoryRecord;
import ASTree.CompilationUnitManipulationMethod;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;
import org.eclipse.jdt.internal.corext.refactoring.code.*;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

public class JavaRefactoringELVBase extends JavaRefactoring{

	ExtractLocalVariableActivity activity;
	
	public JavaRefactoringELVBase(ICompilationUnit u, int l, IMarker m, ExtractLocalVariableActivity a)
			throws Exception {
		super(u, l, m);
		activity = a;
	}

	@SuppressWarnings("restriction")
	@Override
	final protected void performRefactoring(IProgressMonitor pm) throws Exception {
		ICompilationUnit unit = getICompilationUnit();
		unit.becomeWorkingCopy(null);
		int start = activity.getRecord().getSeletectedRegion()[0];
		int length = activity.getRecord().getSeletectedRegion()[1] 
				- activity.getRecord().getSeletectedRegion()[0] + 1;
		ExtractTempRefactoring refactoring = new ExtractTempRefactoring(unit, start, length);
		RefactoringStatus status = refactoring.checkAllConditions(null);
		if(status.isOK())
			refactoring.createChange(null).perform(null);
		unit.commitWorkingCopy(true, null);
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
	final public void postProcess() throws Exception {
		
		
	}

}
