package extract.declaration;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;


import ASTree.CompilationUnitHistoryRecord;
import ASTree.visitors.ASTMethodDecVisitor;

import compare.SourceDiff;
import compare.SourceDiffChange;
import compare.SourceDiffInsert;

public class MethodSignatureDetector extends DecDetector{
	
	@Override
	protected boolean isDecFoundIn(CompilationUnitHistoryRecord record) {
		CompilationUnit tree = record.getASTree();
		ASTMethodDecVisitor mVisitor = new ASTMethodDecVisitor();
		tree.accept(mVisitor);
		if(mVisitor.isInMethod(getEditedLineNumber(record)))
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
