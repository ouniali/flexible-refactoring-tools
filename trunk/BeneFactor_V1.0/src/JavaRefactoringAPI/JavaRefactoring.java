package JavaRefactoringAPI;

import java.util.ArrayList;

import org.eclipse.jdt.core.*;

public abstract class JavaRefactoring implements Runnable {

	static long WAIT_TIME = 2000;
	private ICompilationUnit unit;
	public abstract void performRefactoring() throws Exception;
	protected abstract void performCodeRecovery() throws Exception;
	
	public final void run() {
		try {
			performCodeRecovery();
			Thread.sleep(WAIT_TIME);
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
