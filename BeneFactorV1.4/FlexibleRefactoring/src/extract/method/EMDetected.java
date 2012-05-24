package extract.method;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;

import JavaRefactoringAPI.JavaRefactoring;

abstract class EMDetected
{
	protected EMDetected(){};
	public static EMDetected getInstance(){return null;};
	public abstract JavaRefactoring getRefactoring(ICompilationUnit unit) throws Exception;
	public abstract boolean equals(Object o);
	public abstract void set(Object o);
}

 class ExtractWithCopy extends EMDetected{
	
	ASTEMActivity activity;
	private static ExtractWithCopy detected;
	
	@Override
	public JavaRefactoring getRefactoring(ICompilationUnit unit) throws Exception {
		return activity.getJavaExtractMethodRefactoring(unit);
	}
	
	public static EMDetected getInstance()
	{
		if(detected == null)
			detected = new ExtractWithCopy();
		return detected;
	}

	@Override
	public boolean equals(Object o) {
		return activity.equals((ASTEMActivity)o);
	}
	@Override
	public void set(Object o) {	
		activity = (ASTEMActivity)o;
	}
}

class ExtractWithCut extends EMDetected{
	
	ASTChangeEM change;
	private static ExtractWithCut detected;
	
	@Override
	public JavaRefactoring getRefactoring(ICompilationUnit unit) throws Exception{
		return change.getJavaExtractMethodRefactoring(unit);
	}

	@Override
	public boolean equals(Object o) {
		return change.equals((ASTChangeEM)o);
	}
	
	public static EMDetected getInstance()
	{
		if(detected == null)
			detected = new ExtractWithCut();
		return detected;
	}

	@Override
	public void set(Object o) {
		change = (ASTChangeEM)o;
	}
	
}