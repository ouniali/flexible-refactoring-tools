package JavaRefactoringAPI;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

public class JavaRefactoringExtractMethodActivity extends JavaRefactoring{

	public JavaRefactoringExtractMethodActivity(ICompilationUnit u, int l, IMarker m) {
		super(u, l, m);
	}

	@Override
	protected void performRefactoring(IProgressMonitor pm) throws Exception {
		
	}

	@Override
	protected void performCodeRecovery(IProgressMonitor pm) throws Exception {
		
	}

	@Override
	public int getRefactoringType() {
		return 0;
	}

	@Override
	public void preProcess() {
			
	}

	@Override
	public void postProcess() {
		// TODO Auto-generated method stub
		
	}

}
