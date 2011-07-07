package JavaRefactoringAPI;

import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.*;
import org.eclipse.ltk.core.refactoring.Change;

import compilation.RefactoringChances;
import compilation.UndoRefactoringChances;

public abstract class JavaRefactoring implements Runnable{

	private ICompilationUnit unit;
	private int line;
	private IMarker marker;
	private Change undo;
	
	protected abstract void performRefactoring() throws Exception;
	protected abstract void performCodeRecovery() throws Exception;
	protected final void setUndo(Change u)
	{
		undo = u;
	}
	public final Change getUndo()
	{
		return undo;
	}
	public final synchronized void run() {
		JavaUndoRefactoring unRef;
		try {
			performCodeRecovery();
			performRefactoring();
			unRef = getJavaUndoRefactoring();
			RefactoringChances.clearRefactoringChances();
			UndoRefactoringChances.addUndoRefactoring(unRef);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	public JavaRefactoring(ICompilationUnit u, int l, IMarker m)
	{
		unit = u;
		line = l;
		marker = m;
	}
	public final ICompilationUnit getICompilationUnit()
	{
		try {
			unit.makeConsistent(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unit;
	}
	public final int getLineNumber()
	{
		return line;
	}
	
	public final IMarker getMarker()
	{
		return marker;
	}
	protected JavaUndoRefactoring getJavaUndoRefactoring()
	{
		if(getUndo()!= null)
			return new JavaUndoRefactoring(getICompilationUnit(), getLineNumber(), getRefactoringType(), getUndo() );
		else
			return null;
	}
	
	abstract public int getRefactoringType();
	
	
}
