package JavaRefactoringAPI.extractmethod;

import java.lang.reflect.Modifier;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

import ASTree.CompilationUnitHistoryRecord;
import ExtractMethod.ASTExtractMethodActivity;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;

public class JavaRefactoringExtractMethodActivity extends JavaRefactoring{

	final ASTExtractMethodActivity activity;
	String methodName;
	
	
	public JavaRefactoringExtractMethodActivity(ICompilationUnit u, int l, 
			IMarker m, ASTExtractMethodActivity a) throws Exception{
		super(u, l, m);
		activity = a;
		methodName = JavaRefactoringExtractMethodUtil.getExtractedMethodName(this.getICompilationUnit());
	}

	@Override
	protected void performRefactoring(IProgressMonitor pm) throws Exception {
		JavaRefactoringExtractMethodUtil.performEclipseRefactoring(this.getICompilationUnit(), 
				activity.getCopyStart(), activity.getCopyLength(), 
				Modifier.PRIVATE, methodName, pm);
	}

	@Override
	protected void performCodeRecovery(IProgressMonitor pm) throws Exception {
		return;
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
