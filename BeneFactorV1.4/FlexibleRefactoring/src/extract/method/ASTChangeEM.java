package extract.method;

import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import compare.SourceDiffIdentical;

import userinterface.RefactoringMarker;
import util.ASTUtil;

import ASTree.*;
import ASTree.CUHistory.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.*;
import JavaRefactoringAPI.extract.method.JavaRefactoringExtractMethodChange;

public class ASTChangeEM extends ASTChange {

	int firstCutNodeIndex;	
	int lastCutNodeIndex;
	
	int lastUncutNodeIndexFromStart;
	int firstUncutNodeIndexFromEnd;
	
	int insertPlaceNodeIndex;
	
	public ASTChangeEM( CompilationUnitHistoryRecord or, ASTNode node1, CompilationUnitHistoryRecord nr, ASTNode node2) 
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
		ArrayList<ASTNode> childrenOne = ASTUtil.getChildNodes(nodeOne);
		ArrayList<ASTNode> childrenTwo = ASTUtil.getChildNodes(nodeTwo);
		int childrenOneSize = childrenOne.size();
		int childrenTwoSize = childrenTwo.size();
		int start = EMUtil.getLengthOfCommonnSubnodesFromStart(nodeOne, nodeTwo);
		int end = Math.min(
				EMUtil.getLengthOfCommonnSubnodesFromEnd(nodeOne, nodeTwo),
				childrenTwoSize-start
		);
		
		ASTNode firstDiffNode =childrenOne.get( start );	
		ASTNode lastDiffNode = childrenOne.get(childrenOneSize - end - 1);
		
		index[0] = ASTUtil.getASTNodeIndexInCompilationUnit(firstDiffNode);		
		index[1] = ASTUtil.getASTNodeIndexInCompilationUnit(lastDiffNode);
		return index;
	}
	
	private int getCuttedASTNodeIndexInNodeTwo(ASTNode nodeOne, ASTNode nodeTwo)
	{
		int start = EMUtil.getLengthOfCommonnSubnodesFromStart(nodeOne, nodeTwo);
		ArrayList<ASTNode> childrenTwo = ASTUtil.getChildNodes(nodeTwo);
		ASTNode node; 
		if(start != 0)
			node = 	childrenTwo.get(start-1);
		else
			node = nodeTwo;
		int index = ASTUtil.getASTNodeIndexInCompilationUnit(node);
		return index;		
	}
	
	
	
	public JavaRefactoringExtractMethodChange getJavaExtractMethodRefactoring(ICompilationUnit unit) throws Exception
	{
		int line = getRefactoringMarkerLine(unit);
		return new JavaRefactoringExtractMethodChange(unit, line, this);
	}
	

	public int[] getSelectionStartAndEnd(ICompilationUnit iunit)
	{
		int[] offsets = new int[2];
		CompilationUnit parsedUnit = ASTUtil.parseICompilationUnit(iunit);
		ASTNode nodeOne = ASTUtil.getASTNodeByIndex(parsedUnit, firstCutNodeIndex);
		ASTNode nodeTwo = ASTUtil.getASTNodeByIndex(parsedUnit, lastCutNodeIndex);
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
			e.printStackTrace();
		}
		return null;
	}
	
	

}
