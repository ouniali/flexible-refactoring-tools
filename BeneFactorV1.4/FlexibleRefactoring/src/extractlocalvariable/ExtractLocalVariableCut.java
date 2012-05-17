package extractlocalvariable;

import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;

public class ExtractLocalVariableCut implements ExtractLocalVariableActivity{

	public ExtractLocalVariableCut(CompilationUnitHistoryRecord r)
	{
		System.out.println("ELV: cut.");
	}
	
	public JavaRefactoring getELVRefactoring() {
		return null;
	}

}
