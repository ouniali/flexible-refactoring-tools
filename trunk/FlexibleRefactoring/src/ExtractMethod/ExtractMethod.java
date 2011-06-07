package ExtractMethod;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.*;

import ASTree.ASTreeManipulationMethods;

public class ExtractMethod {
	
	public static boolean isExtractMethodChange(ASTNode nodeOne, ASTNode nodeTwo)
	{
		ArrayList<ASTNode> childrenOne = ASTreeManipulationMethods.getChildNodes(nodeOne);
		ArrayList<ASTNode> childrenTwo = ASTreeManipulationMethods.getChildNodes(nodeTwo);
	
		int sizeBefore = childrenOne.size();
		int sizeAfter = childrenTwo.size();
		
		ASTNode subOne;
		ASTNode subTwo;

		int lastMatchFromStart = sizeAfter-1;
		
		if(sizeBefore <= sizeAfter)
			return false;
		
		for(int i = 0; i< sizeAfter; i++)
		{
			subOne = childrenOne.get(i);
			subTwo = childrenTwo.get(i);
			if(!subOne.subtreeMatch(new ASTMatcher(), subTwo))
			{
				lastMatchFromStart = i-1;
				break;
			}
		}
		
		for(int i = sizeAfter-1; i> lastMatchFromStart; i--)
		{
			subOne = childrenOne.get(i);
			subTwo = childrenTwo.get(i);
			if(!subOne.subtreeMatch(new ASTMatcher(), subTwo))
				return false;
		}
		return true;
	}
}
