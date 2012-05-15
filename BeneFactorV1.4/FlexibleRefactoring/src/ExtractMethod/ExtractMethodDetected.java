package ExtractMethod;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;

import JavaRefactoringAPI.JavaRefactoring;

abstract class ExtractMethodDetected
{
	protected ExtractMethodDetected(){};
	public static ExtractMethodDetected getInstance(){return null;};
	public abstract JavaRefactoring getRefactoring(ICompilationUnit unit) throws Exception;
	public abstract boolean equals(Object o);
	public abstract void set(Object o);
}

class ExtractWithCopy extends ExtractMethodDetected{
	
	ASTExtractMethodActivity activity;
	private static ExtractWithCopy detected;
	
	@Override
	public JavaRefactoring getRefactoring(ICompilationUnit unit) throws Exception {
		return activity.getJavaExtractMethodRefactoring(unit);
	}
	
	public static ExtractMethodDetected getInstance()
	{
		if(detected == null)
			detected = new ExtractWithCopy();
		return detected;
	}

	@Override
	public boolean equals(Object o) {
		return activity.equals((ASTExtractMethodActivity)o);
	}
	@Override
	public void set(Object o) {	
		activity = (ASTExtractMethodActivity)o;
	}
}

class ExtractWithCut extends ExtractMethodDetected{
	
	ASTExtractMethodChangeInformation change;
	private static ExtractWithCut detected;
	
	@Override
	public JavaRefactoring getRefactoring(ICompilationUnit unit) throws Exception{
		return change.getJavaExtractMethodRefactoring(unit);
	}

	@Override
	public boolean equals(Object o) {
		return change.equals((ASTExtractMethodChangeInformation)o);
	}
	
	public static ExtractMethodDetected getInstance()
	{
		if(detected == null)
			detected = new ExtractWithCut();
		return detected;
	}

	@Override
	public void set(Object o) {
		change = (ASTExtractMethodChangeInformation)o;
	}
	
}