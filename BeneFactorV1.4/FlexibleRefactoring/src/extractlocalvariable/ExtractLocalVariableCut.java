package extractlocalvariable;

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
	
	
	public JavaRefactoring getELVRefactoring() {
		return null;
	}

	public CompilationUnitHistoryRecord getRecord() {
		return record;
	}

}
