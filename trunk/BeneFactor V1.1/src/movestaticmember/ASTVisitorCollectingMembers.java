package movestaticmember;

import java.util.ArrayList;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;

import ASTree.ASTreeManipulationMethods;

public class ASTVisitorCollectingMembers extends ASTVisitor{
	

	static public ArrayList<Integer> getStaticFieldDeclarationsIndices(ASTNode node)
	{
		ASTVisitorCollectingMembers visitor = new ASTVisitorCollectingMembers();
		node.accept(visitor);
		return visitor.StaticFieldDeclarationsIndices;
	}
	
	static public ArrayList<String> getStaticFieldDeclarations(ASTNode node)
	{
		ASTVisitorCollectingMembers visitor = new ASTVisitorCollectingMembers();
		node.accept(visitor);
		return visitor.StaticFieldDeclarations;
	}
	
	ArrayList<Integer> StaticFieldDeclarationsIndices;
	ArrayList<String> StaticFieldDeclarations;
	
	private ASTVisitorCollectingMembers()
	{
		StaticFieldDeclarationsIndices = new ArrayList<Integer>();
		StaticFieldDeclarations = new ArrayList<String>();
	}
	
	public boolean visit(FieldDeclaration node) 
	{
		int modifiers = node.getModifiers();
		int index;
		if(Modifier.isStatic(modifiers))
		{
			index = ASTreeManipulationMethods.getASTNodeIndexInCompilationUnit(node);
			StaticFieldDeclarationsIndices.add(new Integer(index));	
			StaticFieldDeclarations.add(node.toString());
		}
		return true;
	}
	
	
	
}
