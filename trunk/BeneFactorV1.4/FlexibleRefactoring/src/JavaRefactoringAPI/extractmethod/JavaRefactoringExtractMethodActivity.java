package JavaRefactoringAPI.extractmethod;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

import ASTree.CompilationUnitHistoryRecord;
import ExtractMethod.ASTExtractMethodActivity;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;

public class JavaRefactoringExtractMethodActivity extends JavaRefactoring{

	final ASTExtractMethodActivity activity;
	
	public JavaRefactoringExtractMethodActivity(ICompilationUnit u, int l, 
			IMarker m, ASTExtractMethodActivity a) {
		super(u, l, m);
		activity = a;
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
