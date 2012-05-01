package JavaRefactoringAPI.extractmethod;

import java.lang.reflect.Modifier;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;

public abstract class JavaRefactoringExtractMethodBase extends JavaRefactoring{

	String methodName;
	int modifier;
	
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
	public abstract void preProcess();

	@Override
	public abstract void postProcess();
	
	public abstract JavaRefactoringExtractMethodBase moveExtractMethodRefactoring(IMarker marker, int l) throws Exception;
}
