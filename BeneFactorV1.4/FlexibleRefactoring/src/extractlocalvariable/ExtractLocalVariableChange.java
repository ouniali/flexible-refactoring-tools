package extractlocalvariable;

import org.eclipse.jdt.core.dom.ASTNode;

import ASTree.ASTChangeInformation;
import ASTree.CompilationUnitHistoryRecord;

public class ExtractLocalVariableChange extends ASTChangeInformation {

	protected ExtractLocalVariableChange(CompilationUnitHistoryRecord or,
			ASTNode node1, CompilationUnitHistoryRecord nr, ASTNode node2) {
		super(or, node1, nr, node2);

	}

}
