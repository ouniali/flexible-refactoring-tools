package extract.declaration;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import compare.SourceDiff;

import util.StringUtil;

import ASTree.CUHistory.CompilationUnitHistoryRecord;
import ASTree.visitors.ASTMethodDecVisitor;
import JavaRefactoringAPI.extract.localvariable.JavaRefactoringELVBase;

public class LVDecDetector extends DecDetector {
	
	int expression_line;
	
	private LVDecDetector(int l)
	{
		expression_line = l;
	}
	
	public static LVDecDetector create(JavaRefactoringELVBase ELVRef)
	{
		return new LVDecDetector(ELVRef.getLineNumber());
	}
	
	@Override
	protected boolean isDecFoundIn(CompilationUnitHistoryRecord record) 
	{
		SourceDiff diff = record.getSourceDiff();
		if(!diff.isLineNumberAvailable())
			return false;
		int line = diff.getLineNumber();
		CompilationUnit tree = record.getASTree();
		ASTMethodDecVisitor mVisitor = new ASTMethodDecVisitor();
		tree.accept(mVisitor);
		String m1 = mVisitor.getMethodDeclarationName(line);
		String m2 = mVisitor.getMethodDeclarationName(expression_line);
		return m1.equals(m2) && !m1.equals(StringUtil.EMPTY_STRING);
	}

	@Override
	protected Declaration getDeclaration(CompilationUnitHistoryRecord record) {
		int line = record.getSourceDiff().getLineNumber();
		String code = getEditedLineText(record);
		return new LVDec(line ,code, record);
	}

}
