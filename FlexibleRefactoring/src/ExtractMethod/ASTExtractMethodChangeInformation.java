package ExtractMethod;

import org.eclipse.jdt.core.dom.ASTNode;

import ASTree.*;
import JavaRefactoringAPI.JavaExtractMethodRefactoring;

public class ASTExtractMethodChangeInformation extends ASTChangeInformation {

	int SelectionStart;
	int SelectionEnd;
	boolean SelectionSet;
	
	public ASTExtractMethodChangeInformation( CompilationUnitHistoryRecord or, ASTNode node1, CompilationUnitHistoryRecord nr, ASTNode node2) 
	{
		super(or, node1, nr, node2);
		SelectionSet = false;
		
		
	}
	
	
	
	public JavaExtractMethodRefactoring getJavaExtractMethodRefactoring()
	{
		return new JavaExtractMethodRefactoring(SelectionStart, SelectionEnd);
	}

}
