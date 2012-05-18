package JavaRefactoringAPI.extractlocalvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;

public class JavaRefactoringELVBase extends JavaRefactoring{

	public JavaRefactoringELVBase(ICompilationUnit u, int l, IMarker m)
			throws Exception {
		super(u, l, m);
	}

	@Override
	protected void performRefactoring(IProgressMonitor pm) throws Exception {
		
		
	}

	@Override
	protected void performCodeRecovery(IProgressMonitor pm) throws Exception {
		
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
