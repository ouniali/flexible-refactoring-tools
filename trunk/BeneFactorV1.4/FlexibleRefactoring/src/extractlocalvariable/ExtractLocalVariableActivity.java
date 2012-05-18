package extractlocalvariable;

import org.eclipse.jdt.core.ICompilationUnit;

import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;

public interface ExtractLocalVariableActivity{
	public JavaRefactoring getELVRefactoring(ICompilationUnit u) throws Exception;
	public CompilationUnitHistoryRecord getRecord();
	
}