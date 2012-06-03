package extract.declaration;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;


import ASTree.CUHistory.CompilationUnitHistoryRecord;
import ASTree.visitors.ASTMethodDecVisitor;

import compare.SourceDiff;
import compare.SourceDiffChange;
import compare.SourceDiffIdentical;
import compare.SourceDiffInsert;

public class MethodDecDetector extends DecDetector{
	
	@Override
	protected boolean isDecFoundIn(CompilationUnitHistoryRecord record) {
		CompilationUnit tree = record.getASTree();
		ASTMethodDecVisitor mVisitor = new ASTMethodDecVisitor();
		tree.accept(mVisitor);
		SourceDiff diff = record.getSourceDiff();
		if(!diff.isLineNumberAvailable())
			return false;
		if(mVisitor.isInMethod(diff.getLineNumber()))
			return false;
		else
			return true;
	}


	@Override
	protected Declaration getDeclaration(CompilationUnitHistoryRecord record) {
		String s = getEditedLineText(record);
		return (Declaration) new MethodDec(s, record);
	}
	
}
