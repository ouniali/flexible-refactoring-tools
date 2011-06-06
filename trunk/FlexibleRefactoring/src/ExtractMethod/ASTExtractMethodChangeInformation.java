package ExtractMethod;

import org.eclipse.jdt.core.dom.ASTNode;

import ASTree.*;

public class ASTExtractMethodChangeInformation extends ASTChangeInformation {

	public ASTExtractMethodChangeInformation(
			CompilationUnitHistoryRecord or, ASTNode node1,
			CompilationUnitHistoryRecord nr, ASTNode node2) {
		super(or, node1, nr, node2);
		
	}

}
