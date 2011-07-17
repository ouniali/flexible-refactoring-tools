package ExtractMethod;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class ASTMethodDeclarationVisitor extends ASTVisitor {

	ArrayList<Integer> methodDeclarationStartLine;
	ArrayList<Integer> methodDeclarationEndLine;

	public ASTMethodDeclarationVisitor() {
		methodDeclarationStartLine = new ArrayList<Integer>();
		methodDeclarationEndLine = new ArrayList<Integer>();
	}

	@Override
	public boolean visit(MethodDeclaration m) {
		CompilationUnit tree = (CompilationUnit) m.getRoot();
		int start = tree.getLineNumber(m.getStartPosition());
		int end = tree.getLineNumber(m.getStartPosition() + m.getLength());
		methodDeclarationStartLine.add(new Integer(start));
		methodDeclarationEndLine.add(new Integer(end));
		return false;
	}

	public boolean isLineNumberInMethodDeclaration(int line) {
		for (int i = 0; i < methodDeclarationStartLine.size(); i++) {
			int start = methodDeclarationStartLine.get(i);
			int end = methodDeclarationEndLine.get(i);
			if (i >= start && i <= end)
				return true;
		}

		return false;
	}
}
