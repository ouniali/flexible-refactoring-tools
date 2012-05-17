package extractlocalvariable;

import org.eclipse.jdt.core.dom.ASTNode;

import ASTree.ASTChangeInformation;
import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;

public class ExtractLocalVariableCopy implements ExtractLocalVariableActivity{
	
	CompilationUnitHistoryRecord record;
	
	ExtractLocalVariableCopy(CompilationUnitHistoryRecord r)
	{
		record = r;
		System.out.println("ELV: copy.");
	}
	
	static private ExtractLocalVariableCopy instance;
	public static ExtractLocalVariableCopy getNewInstance(CompilationUnitHistoryRecord r)
	{
		 instance = new  ExtractLocalVariableCopy(r);
		 return instance;
	}
	public static ExtractLocalVariableCopy getCurrentInstance()
	{
		return instance;
	}
	
	public JavaRefactoring getELVRefactoring() {
		return null;
	}



	public ExtractLocalVariableActivity createInstance(CompilationUnitHistoryRecord r) {
		return new ExtractLocalVariableCopy(r);
	}



	public CompilationUnitHistoryRecord getRecord() {
		return record;
	}


}



interface ExtractLocalVariableActivity{
	public JavaRefactoring getELVRefactoring();
	public CompilationUnitHistoryRecord getRecord();
}
