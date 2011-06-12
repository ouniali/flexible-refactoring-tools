package userinterface;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.ui.text.java.*;
import org.eclipse.ui.*;

import JavaRefactoringAPI.JavaRefactoringType;

public class RefactoringMarker {
	
	public static long createRefactoringMarker(ICompilationUnit unit, int lineNo, int type) throws Exception
	{

		String message = "finish " + JavaRefactoringType.getRefactoringTypeName(type)+" automatically";
		IMarker marker = unit.getResource().createMarker("FlexibleRefactoring.refactoringproblem");
		marker.setAttribute(IMarker.LINE_NUMBER, lineNo);
		marker.setAttribute(IMarker.MESSAGE, message);
		marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
		marker.setAttribute(IMarker.USER_EDITABLE, false);
		return marker.getId();
	}
	
	public static void deleteRefactoringMarker(ICompilationUnit unit, long markerId)
	{
		try {
			IMarker marker = unit.getResource().findMarker(markerId);
			if(marker != null)
				marker.delete();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
