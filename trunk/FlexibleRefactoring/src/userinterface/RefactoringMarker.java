package userinterface;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.ui.*;

import JavaRefactoringAPI.JavaRefactoringType;

public class RefactoringMarker {
	
	public static void createRefactoringMarker(ICompilationUnit unit, int lineNo, int type) throws Exception
	{

		String message = "finish " + JavaRefactoringType.getRefactoringTypeName(type)+" automatically";
		IMarker marker = unit.getResource().createMarker("FlexibleRefactoring.refactoringproblem");
		marker.setAttribute(IMarker.LINE_NUMBER, lineNo);
		marker.setAttribute(IMarker.MESSAGE, message);
		marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
	}
}
