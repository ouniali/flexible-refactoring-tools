package extract.method;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;

import ASTree.CUHistory.CompilationUnitHistoryRecord;

import util.ASTUtil;
import util.StringUtil;

public class EMUtil {

	public static boolean isExtractMethodChange(
			CompilationUnitHistoryRecord oldRecord, ASTNode nodeOne, 
			CompilationUnitHistoryRecord newRecord, ASTNode nodeTwo) {
		if (nodeOne.getNodeType() != ASTNode.BLOCK)
			return false;

		if (nodeTwo.getNodeType() != ASTNode.BLOCK)
			return false;

		int LastIndexFromStart = getLengthOfCommonnSubnodesFromStart(nodeOne,
				nodeTwo);
		int FirstIndexFromEnd = getLengthOfCommonnSubnodesFromEnd(nodeOne,
				nodeTwo);
		int childrenOneSize = ASTUtil.getChildNodes(nodeOne)
				.size();
		int childrenTwoSize = ASTUtil.getChildNodes(nodeTwo)
				.size();

		if(childrenOneSize == 0)
			return false;
		
		String new_source = newRecord.getSourceCode();
		int begin = nodeTwo.getStartPosition();
		int end = begin + nodeTwo.getLength() - 1;
		String block_source = new_source.substring(begin, end).replace('{', ' ').replace('}', ' ');
		boolean isBlockEmpty = StringUtil.isWhiteSpaceString(block_source);
		
		if (childrenTwoSize == 0 && !isBlockEmpty)
			return false;

		if (childrenOneSize > childrenTwoSize
				&& LastIndexFromStart + FirstIndexFromEnd >= childrenTwoSize)
			return true;
		else
			return false;
	}
	
	
	public static int getLengthOfCommonnSubnodesFromStart(ASTNode nodeOne,
			ASTNode nodeTwo) {
		int index = -1;
		ArrayList<ASTNode> childrenOne = ASTUtil
				.getChildNodes(nodeOne);
		ArrayList<ASTNode> childrenTwo = ASTUtil
				.getChildNodes(nodeTwo);
		int size = Math.min(childrenOne.size(), childrenTwo.size());
		ASTNode childOne;
		ASTNode childTwo;

		for (int i = 0; i < size; i++) {
			childOne = childrenOne.get(i);
			childTwo = childrenTwo.get(i);
			if (childOne.subtreeMatch(new ASTMatcher(), childTwo))
				index = i;
			else
				break;
		}
		return index + 1;
	}
	
	public static int getLengthOfCommonnSubnodesFromEnd(ASTNode nodeOne,
			ASTNode nodeTwo) {
		ArrayList<ASTNode> childrenOne = ASTUtil
				.getChildNodes(nodeOne);
		ArrayList<ASTNode> childrenTwo = ASTUtil
				.getChildNodes(nodeTwo);
		int sizeOne = childrenOne.size();
		int sizeTwo = childrenTwo.size();
		int commonSize = Math.min(sizeOne, sizeTwo);
		ASTNode childOne;
		ASTNode childTwo;
		int index = -1;

		for (int i = 0; i < commonSize; i++) {
			childOne = childrenOne.get(sizeOne - i - 1);
			childTwo = childrenTwo.get(sizeTwo - i - 1);
			if (childOne.subtreeMatch(new ASTMatcher(), childTwo))
				index = i;
			else
				break;
		}

		return index + 1;
	}

}
