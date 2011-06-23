package userinterface;

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

import JavaRefactoringAPI.JavaRefactoringType;

public class RefactoringMarker {

	public static String Refactoring_Problem_First_Argument = "refactoring_problem";
	
	public static void addAutomaticRefactoringProposal(ICompilationUnit unit,
			int lineNo, int type) throws Exception {
		
	//	if(!isMarkerExisting(unit, lineNo))
	//		createRefactoringMarker(unit, lineNo);	
		shootRefactoringProblem(unit, lineNo, type);
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

	@SuppressWarnings("restriction")
	private static void shootRefactoringProblem(
			ICompilationUnit unit, int lineNo, int type) throws Exception {

		ProblemFactory proFac = ProblemFactory.getProblemFactory(Locale
				.getDefault());
		char[] fileName = unit.getPath().toOSString().toCharArray();
		int problemID = IProblem.ExternalProblemFixable;
		String[] problemArguments = new String[2];
		problemArguments[0] = Refactoring_Problem_First_Argument;
		problemArguments[1] = Integer.toString(type);
		String[] messageArguments = null;
		int severity = ProblemSeverities.Fatal;
		int startPosition = 0;
		int endPosition = 1;
		int lineNumber = lineNo;
		int columnNumber = 0;
		
		proFac.createProblem(fileName,
						problemID, problemArguments, messageArguments,
						severity, startPosition, endPosition, lineNumber,
						columnNumber);
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
