package extract.localvariable;

import org.eclipse.jdt.core.ICompilationUnit;

import ASTree.CUHistory.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;

public interface ELVActivity{
	public JavaRefactoring getELVRefactoring(ICompilationUnit u) throws Exception;
	public CompilationUnitHistoryRecord getRecord();
	
}