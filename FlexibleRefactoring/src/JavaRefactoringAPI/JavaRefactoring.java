package JavaRefactoringAPI;

import java.util.ArrayList;

import org.eclipse.jdt.core.*;

public abstract class JavaRefactoring implements Runnable {
	public static ArrayList<JavaRefactoring> UnhandledRefactorings = new ArrayList<JavaRefactoring>();
	
	public abstract void setEnvironment(ICompilationUnit unit);
	public abstract void performRefactoring();
	public abstract boolean checkPreconditions();
	public abstract boolean checkPostconditions();
}
