package extractlocalvariable;

import org.eclipse.jdt.core.ICompilationUnit;

import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;

public class ExtractLocalVariableCut implements ExtractLocalVariableActivity{

	CompilationUnitHistoryRecord record;
	
	private ExtractLocalVariableCut(CompilationUnitHistoryRecord r)
	{
		record = r;
	}
	
	static private ExtractLocalVariableCut instance;
	public static ExtractLocalVariableCut getNewInstance(CompilationUnitHistoryRecord r)
	{
		 instance = new  ExtractLocalVariableCut(r);
		 return instance;
	}
	public static ExtractLocalVariableCut getCurrentInstance()
	{
		return instance;
	}
	
	
	
	public CompilationUnitHistoryRecord getRecord() {
		return record;
	}
		
	public JavaRefactoring getELVRefactoring(ICompilationUnit u) {
		return null;
	}

}
