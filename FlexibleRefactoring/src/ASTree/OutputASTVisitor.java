package ASTree;

import org.eclipse.jdt.core.dom.*;


public class OutputASTVisitor extends ASTVisitor {
	
	static int level = 0;
	
	public void preVisit(ASTNode node) 
	{
		for(int i = 0; i<level; i++)
			System.out.print("   ");
		System.out.println(getNodeTypeName(node));
		level++;
	}
	
	public void postVisit(ASTNode node)
	{
		level--;
	}
	private String getNodeTypeName(ASTNode node)
	{
		int type = node.getNodeType();
		Class nodeClass = ASTNode.nodeClassForType(type);
		String nodeName = nodeClass.getName();
		return nodeName;
	}


}
