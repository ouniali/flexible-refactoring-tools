package compilation;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;

import userinterface.RefactoringMarker;
import JavaRefactoringAPI.*;
import JavaRefactoringAPI.extractmethod.JavaRefactoringExtractMethodChange;

public class RefactoringChances {
	
	private static ArrayList<JavaRefactoring> refactorings = new ArrayList<JavaRefactoring>();
	static final int max_size = 5;
	
	public static void addNewRefactoringChance(JavaRefactoring ref)
	{
		if(ref != null)
		{
			if(refactorings.size() == max_size)
				refactorings.remove(0);
			refactorings.add(ref);
		}
			
	}
	public static ArrayList<JavaRefactoring> getJavaRefactorings(ICompilationUnit unit, int line) throws Exception
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
	
	public static JavaRefactoring getLatestJavaRefactoring(ICompilationUnit unit, int line) throws Exception
	{
		ArrayList<JavaRefactoring> refs = getJavaRefactorings(unit, line);
		int index = refs.size() - 1;
		return refs.get(index);
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
	
	public static ArrayList<JavaRefactoring> getPendingExtractMethodRefactoring()
	{
		ArrayList<JavaRefactoring> extracts = new ArrayList<JavaRefactoring>();
		
		for(JavaRefactoring ref : refactorings)
		{
			if(ref.getRefactoringType() == JavaRefactoringType.EXTRACT_METHOD)
				extracts.add(ref);
		}	
		return extracts;	
	}
	
	public static ArrayList<JavaRefactoring> getPendingRenameRefactoring()
	{
		ArrayList<JavaRefactoring> renames = new ArrayList<JavaRefactoring>();
		
		for(JavaRefactoring ref : refactorings)
		{
			if(ref.getRefactoringType() == JavaRefactoringType.RENAME)
				renames.add(ref);
		}	
		return renames;
		
	}
	
	public static void removeRefactoring(JavaRefactoring refactoring)
	{
		try
		{		
			IMarker marker = refactoring.getMarker();
			if(marker.exists() && marker.getType().equals(RefactoringMarker.REFACTORING_MARKER_TYPE))
				marker.delete();
			refactorings.remove(refactoring);
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void main()
	{
	}

	
}
