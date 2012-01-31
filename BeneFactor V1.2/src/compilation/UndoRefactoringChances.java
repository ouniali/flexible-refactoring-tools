package compilation;
import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;

import userinterface.RefactoringMarker;

import JavaRefactoringAPI.*;

public class UndoRefactoringChances {

	static ArrayList<JavaUndoRefactoring> undos = new ArrayList<JavaUndoRefactoring> ();
	
	public static void addUndoRefactoring(JavaUndoRefactoring u)
	{
		undos.add(u);
	}
	
	public static void clearUndos()
	{
		try{
			for(JavaUndoRefactoring u : undos)
			{
				IMarker marker = u.getMarker();
				if(marker != null && marker.exists() && marker.getType().equals(RefactoringMarker.REFACTORING_MARKER_TYPE))
					marker.delete();
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		undos.clear();
	}
	
	public static ArrayList<JavaUndoRefactoring> getUndoRefactoring(ICompilationUnit unit, int l)
	{
		ArrayList<JavaUndoRefactoring> result = new ArrayList<JavaUndoRefactoring>();
		for(JavaUndoRefactoring current: undos)
		{
			if(current.getICompilationUnit().getPath().toOSString().equals(unit.getPath().toOSString()) && l == current.getLineNumber())
				result.add(current);
		}
		return result;
	}
}
