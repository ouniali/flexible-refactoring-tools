package compilation;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import JavaRefactoringAPI.JavaRefactoring;

public class RefactoringChances {
	private static ArrayList<ICompilationUnit> files = new ArrayList<ICompilationUnit>();
	private static ArrayList<Integer> lineNumbers = new ArrayList<Integer>();
	private static ArrayList<JavaRefactoring> refactorings = new ArrayList<JavaRefactoring>();
	private static int size = 0;
	
	public static void addNewRefactoringChance(ICompilationUnit unit, int line, JavaRefactoring ref)
	{
		files.add(unit);
		lineNumbers.add(new Integer(line));
		refactorings.add(ref);
		size ++;
	}
	public static ArrayList<JavaRefactoring> getJavaRefactoring(ICompilationUnit unit, int line)
	{
		ArrayList<JavaRefactoring> results = new ArrayList<JavaRefactoring>();
		for(int i = 0; i< size; i++)
		{
			ICompilationUnit u = files.get(i);
			int l = lineNumbers.get(i).intValue();
			if(u.getPath().toOSString().equals(unit.getPath().toOSString()) && line == l)
				results.add(refactorings.get(i));		
		}
		return results;
	}
	
	
}
