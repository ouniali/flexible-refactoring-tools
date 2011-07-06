package ExtractMethod;

import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import userinterface.RefactoringMarker;

import ASTree.*;
import JavaRefactoringAPI.*;

public class ASTExtractMethodChangeInformation extends ASTChangeInformation {

	int firstCutNodeIndex;
	int lastCutNodeIndex;
	
	int lastUncutNodeIndexFromStart;
	int firstUncutNodeIndexFromEnd;
	
	int insertPlaceNodeIndex;
	
	public ASTExtractMethodChangeInformation( CompilationUnitHistoryRecord or, ASTNode node1, CompilationUnitHistoryRecord nr, ASTNode node2) 
	{
		super(or, node1, nr, node2);
		int[] index = getCutASTNodeIndex(node1, node2);
		firstCutNodeIndex = index[0];
		lastCutNodeIndex = index[1];
		
		insertPlaceNodeIndex = getCuttedASTNodeIndexInNodeTwo(node1, node2);
	}
	private int[] getCutASTNodeIndex(ASTNode nodeOne, ASTNode nodeTwo)
	{
		int[] index = new int[2];
		ArrayList<ASTNode> childrenOne = ASTreeManipulationMethods.getChildNodes(nodeOne);
		ArrayList<ASTNode> childrenTwo = ASTreeManipulationMethods.getChildNodes(nodeTwo);
		int childrenOneSize = childrenOne.size();
		int childrenTwoSize = childrenTwo.size();
		int start = ExtractMethod.getLengthOfCommonnSubnodesFromStart(nodeOne, nodeTwo);
		int end = Math.min(
				ExtractMethod.getLengthOfCommonnSubnodesFromEnd(nodeOne, nodeTwo),
				childrenTwoSize-start
		);
		
		ASTNode firstDiffNode =childrenOne.get( start );	
		ASTNode lastDiffNode = childrenOne.get(childrenOneSize - end - 1);
		
		index[0] = ASTreeManipulationMethods.getASTNodeIndexInCompilationUnit(firstDiffNode);		
		index[1] = ASTreeManipulationMethods.getASTNodeIndexInCompilationUnit(lastDiffNode);
		return index;
	}
	private int getCuttedASTNodeIndexInNodeTwo(ASTNode nodeOne, ASTNode nodeTwo)
	{
		int start = ExtractMethod.getLengthOfCommonnSubnodesFromStart(nodeOne, nodeTwo);
		ArrayList<ASTNode> childrenTwo = ASTreeManipulationMethods.getChildNodes(nodeTwo);
		ASTNode node = childrenTwo.get(start-1);
		int index = ASTreeManipulationMethods.getASTNodeIndexInCompilationUnit(node);
		return index;		
	}
	
	
	
	public JavaRefactoringExtractMethod getJavaExtractMethodRefactoring(ICompilationUnit unit) throws Exception
	{
		int line = getRefactoringMarkerLine(unit);
		IMarker marker = RefactoringMarker.addRefactoringMarkerIfNo(unit, line);
		return new JavaRefactoringExtractMethod(unit, line, marker, this);
	}
	

	public int[] getSelectionStartAndEnd(ICompilationUnit iunit)
	{
		int[] offsets = new int[2];
		CompilationUnit parsedUnit = ASTreeManipulationMethods.parseICompilationUnit(iunit);
		ASTNode nodeOne = ASTreeManipulationMethods.getASTNodeByIndex(parsedUnit, firstCutNodeIndex);
		ASTNode nodeTwo = ASTreeManipulationMethods.getASTNodeByIndex(parsedUnit, lastCutNodeIndex);
		offsets[0] = nodeOne.getStartPosition();
		offsets[1] = nodeTwo.getStartPosition()+ nodeTwo.getLength()-1;
		return offsets;
	}
	public String getCuttedSourceCode(ICompilationUnit unit)
	{
		int[] offsets = getSelectionStartAndEnd(unit);
		String source;
		String cutted;
		try {
			source = unit.getSource();
			cutted = source.substring(offsets[0], offsets[1]+1);
			return  cutted;
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int getRefactoringMarkerLine(ICompilationUnit unit) throws Exception
	{
		CompilationUnit tree = ASTreeManipulationMethods.parseICompilationUnit(unit);
		ASTNode node = ASTreeManipulationMethods.getASTNodeByIndex(tree, insertPlaceNodeIndex);
		int lineNo = tree.getLineNumber(node.getStartPosition());
		return lineNo + 1;
	}

}
