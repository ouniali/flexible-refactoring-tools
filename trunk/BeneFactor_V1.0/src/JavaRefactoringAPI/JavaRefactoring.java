package JavaRefactoringAPI;

import java.util.ArrayList;

import org.eclipse.jdt.core.*;
import org.eclipse.ltk.core.refactoring.Change;

public abstract class JavaRefactoring implements Runnable{

	private ICompilationUnit unit;
	private Change undo;
	public abstract void performRefactoring() throws Exception;
	protected abstract void performCodeRecovery() throws Exception;
	public final void setUndo(Change u)
	{
		undo = u;
	}
	public final Change getUndo()
	{
		return undo;
	}
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
