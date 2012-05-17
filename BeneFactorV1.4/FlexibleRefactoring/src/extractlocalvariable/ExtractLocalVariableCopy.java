package extractlocalvariable;

import org.eclipse.jdt.core.dom.ASTNode;

import ASTree.ASTChangeInformation;
import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;

public class ExtractLocalVariableCopy implements ExtractLocalVariableActivity{

	public ExtractLocalVariableCopy(CompilationUnitHistoryRecord r)
	{
		System.out.println("ELV: copy.");
	}
	
	public JavaRefactoring getELVRefactoring() {
		return null;
	}


}


interface ExtractLocalVariableActivity{
	public JavaRefactoring getELVRefactoring();
}
