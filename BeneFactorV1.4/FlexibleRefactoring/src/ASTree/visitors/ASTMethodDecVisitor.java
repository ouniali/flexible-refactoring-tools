package ASTree.visitors;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class ASTMethodDecVisitor extends ASTVisitor {

	ArrayList<Integer> methodDeclarationStartLine;
	ArrayList<Integer> methodDeclarationEndLine;
	ArrayList<String> methodNames;

	public ASTMethodDecVisitor() {
		methodDeclarationStartLine = new ArrayList<Integer>();
		methodDeclarationEndLine = new ArrayList<Integer>();
		methodNames = new ArrayList<String>();
	}

	@Override
	public boolean visit(MethodDeclaration m) {
		CompilationUnit tree = (CompilationUnit) m.getRoot();
		int start = tree.getLineNumber(m.getStartPosition());
		int end = tree.getLineNumber(m.getStartPosition() + m.getLength() - 1 );
		methodDeclarationStartLine.add(new Integer(start));
		methodDeclarationEndLine.add(new Integer(end));
		methodNames.add(m.getName().getFullyQualifiedName());
		return false;
	}

	public String getMethodDeclarationName(int line) {
		for (int i = 0; i < methodDeclarationStartLine.size(); i++) {
			int start = methodDeclarationStartLine.get(i);
			int end = methodDeclarationEndLine.get(i);
			String name = methodNames.get(i);
			if (line >= start && line <= end)
				return name;
		}
		return "";
	}
	
	public boolean isInMethod(int line)
	{
		return !getMethodDeclarationName(line).equals("");
	}
	

}
