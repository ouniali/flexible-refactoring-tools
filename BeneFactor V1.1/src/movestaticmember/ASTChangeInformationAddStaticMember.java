package movestaticmember;

import org.eclipse.jdt.core.dom.ASTNode;

import ASTree.ASTChangeInformation;
import ASTree.CompilationUnitHistoryRecord;

public class ASTChangeInformationAddStaticMember extends ASTChangeInformation 
{

	String staticFieldDeclaration;
	int staticFieldDeclarationIndex;
	
	public ASTChangeInformationAddStaticMember(
			CompilationUnitHistoryRecord or, ASTNode node1,
			CompilationUnitHistoryRecord nr, ASTNode node2) 
	{
		super(or, node1, nr, node2);
		staticFieldDeclaration = MoveStaticMember.getAddedStaticDeclaration(node1, node2);
		staticFieldDeclarationIndex = MoveStaticMember.getAddedStaticDeclarationIndex(node2, staticFieldDeclaration);
	}
	
	
	
	

}
