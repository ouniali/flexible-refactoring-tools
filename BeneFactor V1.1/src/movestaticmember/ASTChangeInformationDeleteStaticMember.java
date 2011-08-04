package movestaticmember;

import org.eclipse.jdt.core.dom.ASTNode;

import ASTree.ASTChangeInformation;
import ASTree.CompilationUnitHistoryRecord;

public class ASTChangeInformationDeleteStaticMember extends ASTChangeInformation{

	String staticFieldDeclaration;
	int staticFieldDeclarationIndex;
	
	public ASTChangeInformationDeleteStaticMember(
			CompilationUnitHistoryRecord or, ASTNode node1,
			CompilationUnitHistoryRecord nr, ASTNode node2) {
		super(or, node1, nr, node2);		
		staticFieldDeclaration = MoveStaticMember.getAddedStaticDeclaration(node2, node1);
		staticFieldDeclarationIndex = MoveStaticMember.getAddedStaticDeclarationIndex(node1, staticFieldDeclaration);
	}

}
