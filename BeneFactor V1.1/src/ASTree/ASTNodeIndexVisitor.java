package ASTree;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

public class ASTNodeIndexVisitor extends ASTVisitor{
	
	int currentIndex;
	int targetIndex;
	ASTNode target;
	boolean getIndex;
	boolean notFound;
	
	public ASTNodeIndexVisitor(ASTNode no)
	{
		currentIndex = 0;
		target = no;
		getIndex = true;
		notFound = true;
	}
	public ASTNodeIndexVisitor(int index)
	{
		currentIndex = 0;
		targetIndex = index;
		getIndex = false;
		notFound= true;
	}
	
	public void preVisit(ASTNode node) 
	{	
		if(getIndex)
		{
			if(node.equals(target))
			{
				targetIndex = currentIndex;
				notFound = false;
			}
		}
		else
		{
			if(currentIndex == targetIndex)
			{
				target = node;
				notFound = false;
			}
		}
		currentIndex ++;
	}
	
	public ASTNode getTargetNode()
	{
		if(notFound)
			return null;	
		else
			return target;
	}
	public int getTargetNodeIndex()
	{
		if(notFound)
			return -1;
		else
			return targetIndex;
	}
}
