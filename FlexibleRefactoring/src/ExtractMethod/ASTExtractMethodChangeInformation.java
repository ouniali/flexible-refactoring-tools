package ExtractMethod;

import org.eclipse.jdt.core.dom.ASTNode;

import ASTree.ASTChangeInformation;
import ASTree.CompilationUnitHistoryRecord;

public class ASTExtractMethodChangeInformation extends ASTChangeInformation{

	protected ASTExtractMethodChangeInformation(
			CompilationUnitHistoryRecord or, ASTNode node1,
			CompilationUnitHistoryRecord nr, ASTNode node2) {
		super(or, node1, nr, node2);
		// TODO Auto-generated constructor stub
	}

}
