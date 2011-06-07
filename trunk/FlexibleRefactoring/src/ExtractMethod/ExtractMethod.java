package ExtractMethod;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.*;

import ASTree.ASTreeManipulationMethods;

public class ExtractMethod {
	
	public static boolean isExtractMethodChange(ASTNode nodeOne, ASTNode nodeTwo)
	{
		int LastIndexFromStart = getLengthOfCommonnSubnodesFromStart(nodeOne, nodeTwo);
		int FirstIndexFromEnd = getLengthOfCommonnSubnodesFromEnd(nodeOne, nodeTwo);
		int childrenOneSize = ASTreeManipulationMethods.getChildNodes(nodeOne).size();
		int childrenTwoSize = ASTreeManipulationMethods.getChildNodes(nodeTwo).size();
		
		if(childrenOneSize> childrenTwoSize && LastIndexFromStart + FirstIndexFromEnd  >= childrenTwoSize)
			return true;
		else
			return false;
	}
	
	public static int getLengthOfCommonnSubnodesFromStart(ASTNode nodeOne, ASTNode nodeTwo)
	{
		int index = -1;
		ArrayList<ASTNode> childrenOne = ASTreeManipulationMethods.getChildNodes(nodeOne);
		ArrayList<ASTNode> childrenTwo = ASTreeManipulationMethods.getChildNodes(nodeTwo);
		int size = Math.min(childrenOne.size(),childrenTwo.size());
		ASTNode childOne;
		ASTNode childTwo;
		
		for(int i = 0; i< size; i++)
		{
			childOne = childrenOne.get(i);
			childTwo = childrenTwo.get(i);
			if(childOne.subtreeMatch(new ASTMatcher(), childTwo))
				index = i;
			else
				break;
		}		
		return index+1;			
	}
	
	public static int getLengthOfCommonnSubnodesFromEnd(ASTNode nodeOne, ASTNode nodeTwo)
	{
		ArrayList<ASTNode> childrenOne = ASTreeManipulationMethods.getChildNodes(nodeOne);
		ArrayList<ASTNode> childrenTwo = ASTreeManipulationMethods.getChildNodes(nodeTwo);
		int sizeOne = childrenOne.size();
		int sizeTwo = childrenTwo.size();
		int commonSize = Math.min(sizeOne, sizeTwo);
		ASTNode childOne;
		ASTNode childTwo;
		int index = -1;
		
		for(int i = 0; i<commonSize; i++)
		{
			childOne = childrenOne.get(sizeOne-i-1);
			childTwo = childrenTwo.get(sizeTwo-i-1);
			if(childOne.subtreeMatch(new ASTMatcher(), childTwo))
				index = i;
			else
				break;
		}
		
		return index+1;
	}
}
