package movestaticmember;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import util.ASTUtil;

import ASTree.ASTChange;
import ASTree.CUHistory.CompilationUnitHistoryRecord;

public class ASTChangeDeleteStaticMember extends ASTChange{

	String staticFieldDeclaration;
	int staticFieldDeclarationIndex;
	String fieldName;
	int fieldNameIndex;
	int fieldNameStart;
	int fieldNameLength;
	
	public ASTChangeDeleteStaticMember(
			CompilationUnitHistoryRecord or, ASTNode node1,
			CompilationUnitHistoryRecord nr, ASTNode node2) 
	{
		super(or, node1, nr, node2);
		staticFieldDeclaration = MoveStaticMember.getAddedStaticDeclaration(node2, node1);
		staticFieldDeclarationIndex = MoveStaticMember.getAddedStaticDeclarationIndex(node1, staticFieldDeclaration);
		prepareNameIndex();
		System.out.print(staticFieldDeclaration);
	}
	
	public String getStaticFieldDeclaration()
	{
		return staticFieldDeclaration;
	}
	
	public IMember getMovedStaticField() throws Exception
	{
		return getMovedStaticField2();
	}
	
	public IMember getMovedStaticField1() throws Exception
	{
		ICompilationUnit unit = getICompilationUnit();
		IType type = unit.getAllTypes()[0];
		IField[] fields = type.getFields();
		for(IField field : fields)
		{
			String s = field.getSource();
			if(staticFieldDeclaration.startsWith(s))
				return field;
		}
		return null;
	}
	
	public IMember getMovedStaticField2() throws Exception
	{
		ICompilationUnit unit = this.getICompilationUnit();
		IJavaElement ele = unit.codeSelect(fieldNameStart, fieldNameLength)[0];
		return (IMember)ele;
	}
	
	private void prepareNameIndex()
	{
		CompilationUnit tree = getOldCompilationUnitRecord().getASTree();
		ASTNode parent = ASTUtil.getASTNodeByIndex(tree, staticFieldDeclarationIndex);
		ArrayList<ASTNode> children = ASTUtil.getOffsprings(parent);
		for(ASTNode kid: children)
		{
			if(kid instanceof SimpleName)
			{
				SimpleName name = (SimpleName) kid;
				fieldName = name.getIdentifier();
				fieldNameIndex = ASTUtil.getASTNodeIndexInCompilationUnit(kid);
				fieldNameStart = kid.getStartPosition();
				fieldNameLength = kid.getLength();
				return;
			}
		}
		
	}

}
