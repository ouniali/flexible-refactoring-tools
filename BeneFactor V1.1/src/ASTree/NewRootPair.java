package ASTree;

import org.eclipse.jdt.core.dom.ASTNode;

public class NewRootPair
{
	public boolean RootsChanged;
	public ASTNode nodeOne;
	public ASTNode nodeTwo;
	
	public NewRootPair(boolean changed, ASTNode one, ASTNode two)
	{
		RootsChanged = changed;
		nodeOne = one;
		nodeTwo = two;
	}
}