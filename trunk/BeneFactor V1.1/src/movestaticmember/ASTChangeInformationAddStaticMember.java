package movestaticmember;

import org.eclipse.jdt.core.dom.ASTNode;

import ASTree.ASTChangeInformation;
import ASTree.CompilationUnitHistoryRecord;

public class ASTChangeInformationAddStaticMember extends ASTChangeInformation 
{

	protected ASTChangeInformationAddStaticMember(
			CompilationUnitHistoryRecord or, ASTNode node1,
			CompilationUnitHistoryRecord nr, ASTNode node2) {
		super(or, node1, nr, node2);
	}

}
