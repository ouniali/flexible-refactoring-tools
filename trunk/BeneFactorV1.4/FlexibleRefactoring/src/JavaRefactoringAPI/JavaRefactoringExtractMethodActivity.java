package JavaRefactoringAPI;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

import ASTree.CompilationUnitHistoryRecord;

public class JavaRefactoringExtractMethodActivity extends JavaRefactoring{

	final CompilationUnitHistoryRecord record;
	
	public JavaRefactoringExtractMethodActivity(ICompilationUnit u, int l, 
			IMarker m, CompilationUnitHistoryRecord r) {
		super(u, l, m);
		record = r;
	}

	@Override
	protected void performRefactoring(IProgressMonitor pm) throws Exception {
		
	}

	@Override
	protected void performCodeRecovery(IProgressMonitor pm) throws Exception {
		
	}

	@Override
	public int getRefactoringType() {
		return JavaRefactoringType.EXTRACT_METHOD;
	}

	@Override
	public void preProcess() {
			
	}

	@Override
	public void postProcess() {
	
	}

}
