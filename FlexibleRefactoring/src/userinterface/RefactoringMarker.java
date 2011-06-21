package userinterface;

import java.util.Locale;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.core.builder.ProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;
import org.eclipse.jdt.ui.text.java.*;
import org.eclipse.ui.*;

import JavaRefactoringAPI.JavaRefactoringType;

public class RefactoringMarker {

	public static void addAutomaticRefactoringResolution(ICompilationUnit unit,
			int lineNo, int type) throws Exception {
		if (!addRefactoringResolutionToExistingMarker(unit, lineNo, type))
			createRefactoringMarker(unit, lineNo, type);
	}

	public static long createRefactoringMarker(ICompilationUnit unit,
			int lineNo, int type) throws Exception {

		String message = "finish "
				+ JavaRefactoringType.getRefactoringTypeName(type)
				+ " automatically";
		IMarker marker = unit.getResource().createMarker(
				"FlexibleRefactoring.refactoringproblem");
		marker.setAttribute(IMarker.LINE_NUMBER, lineNo);
		marker.setAttribute(IMarker.MESSAGE, message);
		marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
		marker.setAttribute(IMarker.USER_EDITABLE, false);
		marker.setAttribute("REFACTORING_TYPE", type);
		return marker.getId();
	}

	@SuppressWarnings("restriction")
	public static boolean addRefactoringResolutionToExistingMarker(
			ICompilationUnit unit, int lineNo, int type) throws Exception {
		IMarker markers[] = unit.getResource().findMarkers(IMarker.MARKER,
				true, 1);
		ProblemFactory proFac = ProblemFactory.getProblemFactory(Locale
				.getDefault());
		char[] fileName = unit.getPath().toOSString().toCharArray();
		int problemID = IProblem.ExternalProblemFixable;
		String[] problemArguments = new String[1];
		problemArguments[0] = Integer.toString(type);
		String[] messageArguments = null;
		int severity = ProblemSeverities.Optional;
		int startPosition = 0;
		int endPosition = 1;
		int lineNumber = lineNo;
		int columnNumber = 0;

		int markerLine;
		for (IMarker marker : markers) {
			markerLine = marker.getAttribute(IMarker.LINE_NUMBER, -1);
			if (markerLine == lineNo) {
				proFac.createProblem(fileName,
						problemID, problemArguments, messageArguments,
						severity, startPosition, endPosition, lineNumber,
						columnNumber);
				return true;
			}
		}
		return false;
	}

	public static void deleteRefactoringMarker(ICompilationUnit unit,
			long markerId) {
		try {
			IMarker marker = unit.getResource().findMarker(markerId);
			if (marker != null)
				marker.delete();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
