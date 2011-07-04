package userinterface;

import java.util.ArrayList;
import java.util.Locale;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.core.builder.ProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;
import org.eclipse.jdt.ui.text.java.*;
import org.eclipse.ui.*;
import org.eclipse.jdt.internal.compiler.CompilationResult;

public class RefactoringMarker {

	public static void addRefactoringMarkerIfNo(ICompilationUnit unit,
			int lineNo) throws Exception {
		if(!isMarkerExisting(unit, lineNo))
			createRefactoringMarker(unit, lineNo);
	}

	public static long createRefactoringMarker(ICompilationUnit unit,
			int lineNo) throws Exception {
		String message = "Benefactor message";
		IMarker marker = unit.getResource().createMarker(
				"FlexibleRefactoring.refactoringproblem");
		marker.setAttribute(IMarker.LINE_NUMBER, lineNo);
		marker.setAttribute(IMarker.MESSAGE, message);
		marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
		marker.setAttribute(IMarker.USER_EDITABLE, false);
		return marker.getId();
	}


	public static void deleteRefactoringMarker(ICompilationUnit unit,
			int line) {
		IMarker[] markers;
		try {
			markers = unit.getResource().findMarkers(IMarker.MARKER, true, IResource.DEPTH_INFINITE);
			int index;
			for( index = 0; index < markers.length; index++)
			{
				IMarker marker = markers[index];
				int lineNo = marker.getAttribute(IMarker.LINE_NUMBER, -1);
				if(line == lineNo)
					break;
			}
			if(index<markers.length)
				markers[index].delete();
		} catch (Exception e1) {
			e1.printStackTrace();
		}	
	}
	
	public static boolean isMarkerExisting(ICompilationUnit unit, int line) throws Exception
	{
		IMarker[] markers = unit.getResource().findMarkers(IMarker.MARKER, true, IResource.DEPTH_INFINITE);
		for(IMarker marker : markers)
		{
			int lineNo = marker.getAttribute(IMarker.LINE_NUMBER, -1);
			if(line == lineNo)
				return true;
		}
		return false;
	}
}
