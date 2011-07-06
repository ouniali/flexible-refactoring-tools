package JavaRefactoringAPI;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.ltk.core.refactoring.Change;

import userinterface.RefactoringMarker;

public class JavaUndoRefactoring implements Runnable{
	
	Change undo;
	ICompilationUnit unit;
	int line;
	IMarker marker;
	
	public JavaUndoRefactoring(ICompilationUnit u, int l ,Change d)
	{
		unit = u;
		line = l;
		undo = d;
		marker = RefactoringMarker.addRefactoringMarkerIfNo(unit, line);
	}

	@Override
	public void run() {		
		try {
			undo.perform(null);
		} catch (CoreException e) {
			e.printStackTrace();
		}	
	}
	
}
