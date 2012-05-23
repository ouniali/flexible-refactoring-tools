package compilation;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;

import userinterface.RefactoringMarker;
import JavaRefactoringAPI.*;
import JavaRefactoringAPI.extractlocalvariable.JavaRefactoringELVBase;
import JavaRefactoringAPI.extractmethod.JavaRefactoringExtractMethodBase;
import JavaRefactoringAPI.extractmethod.JavaRefactoringExtractMethodChange;

public class RefactoringChances {
	
	private List<JavaRefactoring> refactorings = new ArrayList<JavaRefactoring>();
	private static final int max_size = 5;
	private static RefactoringChances instance = new RefactoringChances();
	
	private RefactoringChances(){};
	
	public static RefactoringChances getInstance()
	{
		return instance;
	}
	
	
	public synchronized void addNewRefactoringChance(JavaRefactoring ref)
	{
		if(refactorings.size() == max_size)
			refactorings.remove(0);
		refactorings.add(ref);
	}

	public synchronized List<JavaRefactoring> getJavaRefactorings(ICompilationUnit unit, int line) throws Exception
	{
		List<JavaRefactoring> results = new ArrayList<JavaRefactoring>();
		
		for(JavaRefactoring refactoring: refactorings)
		{
			ICompilationUnit u = refactoring.getICompilationUnit();
			int l = refactoring.getLineNumber();
			if(u.getPath().toOSString().equals(unit.getPath().toOSString()) && line == l)
				results.add(refactoring);
		}
		return results;	
	}
	
	public boolean hasRefactorings(ICompilationUnit unit, int line) throws Exception
	{
		List<JavaRefactoring> refs = getJavaRefactorings(unit, line);
		return refs.size() > 0;
	}
	
	public synchronized JavaRefactoring getLatestJavaRefactoring(ICompilationUnit unit, int line) throws Exception
	{
		List<JavaRefactoring> refs = getJavaRefactorings(unit, line);
		int index = refs.size() - 1;
		return refs.get(index);	
	}
	
	
	public void clearRefactoringChances() throws Exception
	{
		for(JavaRefactoring refactoring: refactorings)
		{
			IMarker marker = refactoring.getMarker();
			if(marker.exists() && marker.getType().equals(RefactoringMarker.REFACTORING_MARKER_TYPE))
				marker.delete();
		}
		refactorings.clear();
	}
	
	public List<JavaRefactoring> getPendingEMRefactoring()
	{
		List<JavaRefactoring> extracts = new ArrayList<JavaRefactoring>();
		for(JavaRefactoring ref : refactorings)
		{
			if(ref.getRefactoringType() == JavaRefactoringType.EXTRACT_METHOD)
				extracts.add(ref);
		}	
		return extracts;	
	}
	
	public JavaRefactoringExtractMethodBase getLatestEM()
	{
		
		return (JavaRefactoringExtractMethodBase) getPendingEMRefactoring().
				get(getPendingEMRefactoring().size() - 1);
	}
	
	public List<JavaRefactoring> getPendingRenameRefactoring()
	{
		List<JavaRefactoring> renames = new ArrayList<JavaRefactoring>();
		for(JavaRefactoring ref : refactorings)
		{
			if(ref.getRefactoringType() == JavaRefactoringType.RENAME)
				renames.add(ref);
		}	
		return renames;
	}
	
	public List<JavaRefactoring> getPendingELVRefactoring()
	{
		List<JavaRefactoring> elvs = new ArrayList<JavaRefactoring>();
		for(JavaRefactoring ref : refactorings)
		{
			if(ref.getRefactoringType() == JavaRefactoringType.EXTRACT_LOCAL_VARIABLE)
				elvs.add(ref);
		}	
		return elvs;
	}
	
	public JavaRefactoringELVBase getLatestELV()
	{
		List<JavaRefactoring> list = getPendingELVRefactoring();
		return (JavaRefactoringELVBase)list.get(list.size() - 1);
	}
	
	public void removeRefactoring(JavaRefactoring refactoring) throws Exception
	{
			IMarker marker = refactoring.getMarker();
			if(marker.exists() && marker.getType().equals(RefactoringMarker.REFACTORING_MARKER_TYPE))
				marker.delete();
			refactorings.remove(refactoring);
	}
	

	
}
