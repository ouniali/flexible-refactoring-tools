package ExtractMethod;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import ASTree.*;
import JavaRefactoringAPI.JavaExtractMethodRefactoring;

public class ASTExtractMethodChangeInformation extends ASTChangeInformation {

	int firstCutNodeIndex;
	int lastCutNodeIndex;
	
	public ASTExtractMethodChangeInformation( CompilationUnitHistoryRecord or, ASTNode node1, CompilationUnitHistoryRecord nr, ASTNode node2) 
	{
		super(or, node1, nr, node2);
		int[] index = getCutASTNodeIndex(node1, node2);
		firstCutNodeIndex = index[0];
		lastCutNodeIndex = index[1];
		
	}
	private int[] getCutASTNodeIndex(ASTNode nodeOne, ASTNode nodeTwo)
	{
		int[] index = new int[2];
		ArrayList<ASTNode> childrenOne = ASTreeManipulationMethods.getChildNodes(nodeOne);
		int childrenOneSize = childrenOne.size();
		ASTNode firstDiffNode =childrenOne.get( ExtractMethod.getLengthOfCommonnSubnodesFromStart(nodeOne, nodeTwo) );
		ASTNode lastDiffNode = childrenOne.get(childrenOneSize - ExtractMethod.getLengthOfCommonnSubnodesFromEnd(nodeOne, nodeTwo) - 1);
		index[0] = ASTreeManipulationMethods.getASTNodeIndexInCompilationUnit(firstDiffNode);
		index[1] = ASTreeManipulationMethods.getASTNodeIndexInCompilationUnit(lastDiffNode);		
		return index;
	}
	
	public JavaExtractMethodRefactoring getJavaExtractMethodRefactoring()
	{
		return new JavaExtractMethodRefactoring(this);
	}
	

	public int[] getSelectionStartAndEnd(ICompilationUnit iunit)
	{
		int[] offsets = new int[2];
		CompilationUnit parsedUnit = ASTreeManipulationMethods.parseICompilationUnit(iunit);
		ASTNode nodeOne = ASTreeManipulationMethods.getASTNodeByIndex(parsedUnit, firstCutNodeIndex);
		ASTNode nodeTwo = ASTreeManipulationMethods.getASTNodeByIndex(parsedUnit, lastCutNodeIndex);
		offsets[0] = nodeOne.getStartPosition();
		offsets[1] = nodeTwo.getStartPosition()+ nodeTwo.getLength() - 1;
		return offsets;
	}

}
