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

import flexiblerefactoring.BeneFactor;

public class RefactoringMarker {

	public static final String REFACTORING_MARKER_TYPE = BeneFactor.PlugInID + ".refactoringproblem";
	
	public static IMarker addRefactoringMarkerIfNo(ICompilationUnit unit,
			int lineNo) {
		try{
		ArrayList<IMarker> markers = getMarkers(unit, lineNo);
		if(markers.size() == 0)
			return createRefactoringMarker(unit, lineNo);
		else
			return markers.get(0);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static IMarker createRefactoringMarker(ICompilationUnit unit,
			int lineNo) throws Exception {
		String message = "Benefactor message";
		IMarker marker = unit.getResource().createMarker(
				REFACTORING_MARKER_TYPE);
		marker.setAttribute(IMarker.LINE_NUMBER, lineNo);
		marker.setAttribute(IMarker.MESSAGE, message);
		marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
		marker.setAttribute(IMarker.USER_EDITABLE, false);
		marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
		return marker;
	}



	public static ArrayList<IMarker> getMarkers(ICompilationUnit unit, int line) throws Exception
	{
		ArrayList<IMarker> markersInLine = new ArrayList<IMarker>();
		IMarker[] markers = unit.getResource().findMarkers(IMarker.MARKER, true, IResource.DEPTH_INFINITE);
		for(IMarker marker : markers)
		{
			int lineNo = marker.getAttribute(IMarker.LINE_NUMBER, -1);
			if(line == lineNo)
				markersInLine.add(marker);
		}
		return markersInLine;
	}
}
