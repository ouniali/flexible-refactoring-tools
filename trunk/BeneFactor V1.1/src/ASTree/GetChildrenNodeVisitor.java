package ASTree;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

public class GetChildrenNodeVisitor extends ASTVisitor {
	
	ASTNode parent;
	ArrayList<ASTNode> children;
	
	public GetChildrenNodeVisitor(ASTNode p)
	{
		parent = p;
		children = new ArrayList<ASTNode>();		
	}
	public void preVisit(ASTNode node) {
		
		if(node.getParent()==null)
			return;
		if(node.getParent().equals(parent))
			children.add(node);
		else
			return;
	}
	
	public void postVisit(ASTNode node) {
		// default implementation: do nothing
	}
	
	 public ArrayList<ASTNode> getChildrenList()
	 {
		 return children;
	 }
	

}
