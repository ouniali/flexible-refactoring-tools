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
import JavaRefactoringAPI.extract.localvariable.JavaRefactoringELVBase;
import JavaRefactoringAPI.extract.method.JavaRefactoringExtractMethodBase;

public class RefactoringChances {
	
	private final List<JavaRefactoring> refactorings_rename;
	private final List<JavaRefactoring> refactorings_em;
	private final List<JavaRefactoring> refactorings_elv;
	private final List<JavaRefactoring> refactorings_move;
	
	private static final int max_rename = 5;
	private static final int max_em = 1;
	private static final int max_elv = 1;
	private static final int max_move = 1;
	
	private static RefactoringChances instance = new RefactoringChances();

	private RefactoringChances()
	{
		refactorings_rename = new ArrayList<JavaRefactoring>();
		refactorings_em = new ArrayList<JavaRefactoring>();
		refactorings_elv = new ArrayList<JavaRefactoring>();
		refactorings_move = new ArrayList<JavaRefactoring>();
	};
	
	public static RefactoringChances getInstance()
	{
		return instance;
	}
	
	private void add_refactoring(List l, int max, JavaRefactoring ref)
	{
		if(l.size() == max)
			l.remove(0);
		l.add(ref);
	}
	
	
	public synchronized void addRefactoringChance(JavaRefactoring ref) throws Exception
	{
		switch(ref.getRefactoringType())
		{
		case JavaRefactoringType.RENAME:
			add_refactoring(refactorings_rename, max_rename, ref);
			break;
		case JavaRefactoringType.EXTRACT_LOCAL_VARIABLE:
			add_refactoring(refactorings_elv, max_elv, ref);
			break;
		case JavaRefactoringType.EXTRACT_METHOD:
			add_refactoring(refactorings_em, max_em, ref);
			break;
		case JavaRefactoringType.MOVE_STATIC:
			add_refactoring(refactorings_move, max_move, ref);
			break;
		default:
			throw new Exception ("unknown refactoring type.");
		}
	}
	
	private List<JavaRefactoring> get_all_refactorings()
	{
		List<JavaRefactoring> all = new ArrayList<JavaRefactoring>();
		all.addAll(refactorings_rename);
		all.addAll(refactorings_em);
		all.addAll(refactorings_elv);
		all.addAll(refactorings_move);
		return all;
	}

	public synchronized List<JavaRefactoring> getJavaRefactorings(ICompilationUnit unit, int line) throws Exception
	{
		List<JavaRefactoring> results = new ArrayList<JavaRefactoring>();
		List<JavaRefactoring> all = get_all_refactorings();
		for(JavaRefactoring refactoring: all)
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
		List<JavaRefactoring> all = get_all_refactorings();
		for(JavaRefactoring refactoring: all)
		{
			IMarker marker = refactoring.getMarker();
			if(marker.exists() && marker.getType().equals(RefactoringMarker.REFACTORING_MARKER_TYPE))
				marker.delete();
		}
		refactorings_rename.clear();
		refactorings_em.clear();
		refactorings_elv.clear();
		refactorings_move.clear();
	}
	
	public boolean hasPendingEMRefactorings()
	{
		return !refactorings_em.isEmpty();
	}
	
	public List<JavaRefactoring> getPendingEMRefactorings()
	{
		return refactorings_em;	
	}
	
	public JavaRefactoringExtractMethodBase getLatestEM()
	{
		return (JavaRefactoringExtractMethodBase) 
				refactorings_em.get(refactorings_em.size() - 1);
	}
	
	public boolean hasPendingRenameRefactoring()
	{
		return !refactorings_rename.isEmpty();
	}
	
	public List<JavaRefactoring> getPendingRenameRefactorings()
	{
		return refactorings_rename;
	}
	
	public boolean hasPendingELVRefactoring()
	{
		return !refactorings_elv.isEmpty();
	}
	
	public List<JavaRefactoring> getPendingELVRefactorings()
	{
		return refactorings_elv;
	}
	
	public JavaRefactoringELVBase getLatestELV()
	{
		return (JavaRefactoringELVBase)refactorings_elv.get(refactorings_elv.size() - 1);
	}
	
	
}
