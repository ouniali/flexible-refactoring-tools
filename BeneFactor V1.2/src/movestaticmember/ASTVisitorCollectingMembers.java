package movestaticmember;

import java.util.ArrayList;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;

import ASTree.ASTreeManipulationMethods;

public class ASTVisitorCollectingMembers extends ASTVisitor{
	
	ArrayList<Integer> StaticFieldDeclarationsIndices;
	ArrayList<String> StaticFieldDeclarations;
	ArrayList<Integer> StaticFieldNameIndices;
	
	private ASTVisitorCollectingMembers()
	{
		StaticFieldDeclarationsIndices = new ArrayList<Integer>();
		StaticFieldDeclarations = new ArrayList<String>();
		StaticFieldNameIndices = new ArrayList<Integer>();
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
		ArrayList<ASTNode> children = ASTreeManipulationMethods.getChildNodes(node);
		for(ASTNode kid: children)
		{
			if(kid instanceof Name)
				ASTreeManipulationMethods.getASTNodeIndexInCompilationUnit(kid);
		}
		return true;
	}
	
	public int size()
	{
		return StaticFieldDeclarations.size();
	}
	

	
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
	
	static public ArrayList<StaticFieldDeclarationInformation> getStaticFieldDeclarationInformation(ASTNode node)
	{
		ASTVisitorCollectingMembers visitor = new ASTVisitorCollectingMembers();
		node.accept(visitor);
		ArrayList<StaticFieldDeclarationInformation> results = new ArrayList<StaticFieldDeclarationInformation>();
		for(int i = 0; i<visitor.size(); i++)
		{
			String dec = visitor.StaticFieldDeclarations.get(i);
			int dec_index = visitor.StaticFieldDeclarationsIndices.get(i);
			int name_index = visitor.StaticFieldNameIndices.get(i);
			StaticFieldDeclarationInformation infor = new StaticFieldDeclarationInformation(dec, dec_index, name_index);
			results.add(infor);
		}
		
		return results;
	}
	
	
}
