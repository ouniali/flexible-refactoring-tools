package compilation;

import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;

import userinterface.RefactoringMarker;
import JavaRefactoringAPI.JavaRefactoring;

public class RefactoringChances {
	
	private static ArrayList<JavaRefactoring> refactorings = new ArrayList<JavaRefactoring>();
	
	public static void addNewRefactoringChance(JavaRefactoring ref)
	{
		refactorings.add(ref);
	}
	public static ArrayList<JavaRefactoring> getJavaRefactoring(ICompilationUnit unit, int line)
	{
		ArrayList<JavaRefactoring> results = new ArrayList<JavaRefactoring>();
		
		for(JavaRefactoring refactoring: refactorings)
		{
			ICompilationUnit u = refactoring.getICompilationUnit();
			int l = refactoring.getLineNumber();
			if(u.getPath().toOSString().equals(unit.getPath().toOSString()) && line == l)
				results.add(refactoring);
		}
		return results;	
	}
	
	public static void clearRefactoringChances()
	{
		try
		{
			for(JavaRefactoring refactoring: refactorings)
			{
				IMarker marker = refactoring.getMarker();
				if(marker.exists() && marker.getType().equals(RefactoringMarker.REFACTORING_MARKER_TYPE))
					marker.delete();
			}
			refactorings.clear();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
