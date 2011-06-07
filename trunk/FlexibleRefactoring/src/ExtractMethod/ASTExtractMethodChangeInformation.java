package ExtractMethod;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

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
		or.getASTree().toString();
		
	}
	private int[] getCutASTNodeIndex(CompilationUnit unitOne, CompilationUnit unitTwo)
	{
		int[] index = new int[2];
		
		
		
		return index;
	}
	
	
	
	public JavaExtractMethodRefactoring getJavaExtractMethodRefactoring()
	{
		return new JavaExtractMethodRefactoring(SelectionStart, SelectionEnd);
	}
	
	public void recoverICompilationUnitToBeforeCutting(ICompilationUnit unit)
	{
		String code = getOldCompilationUnitRecord().getASTree().toString();
		ICompilationUnitManipulationMethod.UpdateICompilationUnit(unit, code);
	}

}
