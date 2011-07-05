package JavaRefactoringAPI;

import java.util.ArrayList;

import org.eclipse.jdt.core.*;

public abstract class JavaRefactoring implements Runnable {

	private ICompilationUnit unit;
	public abstract void performRefactoring() throws Exception;
	protected abstract void performCodeRecovery() throws Exception;
	
	public final synchronized void run() {
		try {
			performCodeRecovery();
			performRefactoring();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public JavaRefactoring(ICompilationUnit u)
	{
		unit = u;
	}
	protected final ICompilationUnit getICompilationUnit()
	{
		try {
			unit.makeConsistent(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unit;
	}
	
}
