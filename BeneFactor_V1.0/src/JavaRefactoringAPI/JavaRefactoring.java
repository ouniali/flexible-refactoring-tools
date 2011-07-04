package JavaRefactoringAPI;

import java.util.ArrayList;

import org.eclipse.jdt.core.*;

public abstract class JavaRefactoring implements Runnable {
	private ICompilationUnit unit;
	public abstract void performRefactoring() throws Exception;
	public abstract boolean checkPreconditions();
	public abstract boolean checkPostconditions();
	protected abstract void performCodeRecovery() throws Exception;
	public JavaRefactoring(ICompilationUnit u)
	{
		unit = u;
	}
	protected ICompilationUnit getICompilationUnit()
	{
		try {
			unit.makeConsistent(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unit;
	}
}
