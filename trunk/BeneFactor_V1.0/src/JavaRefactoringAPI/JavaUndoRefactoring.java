package JavaRefactoringAPI;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.text.Assert;
import org.eclipse.ltk.core.refactoring.Change;

import compilation.UndoRefactoringChances;

import userinterface.RefactoringMarker;

public class JavaUndoRefactoring implements Runnable{
	
	Change undo;
	ICompilationUnit unit;
	int line;
	IMarker marker;
	int type;
	
	public JavaUndoRefactoring(ICompilationUnit u, int l, int type ,Change d)
	{	
		unit = u;
		line = l;
		undo = d;
		marker = RefactoringMarker.addRefactoringMarkerIfNo(unit, line);
	}
	public ICompilationUnit getICompilationUnit()
	{
		return unit;
	}
	
	public int getLineNumber()
	{
		return line;
	}
	public int getRefactoringType()
	{
		return type;
	}
	public IMarker getMarker()
	{
		return marker;
	}
	@Override
	public void run() {		
		try {
			NullProgressMonitor monitor = new NullProgressMonitor();
			undo.perform(monitor);
			UndoRefactoringChances.clearUndos();
		} catch (CoreException e) {
			e.printStackTrace();
		}	
	}
	
}
