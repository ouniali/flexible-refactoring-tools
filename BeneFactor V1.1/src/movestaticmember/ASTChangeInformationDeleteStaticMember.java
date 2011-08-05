package movestaticmember;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import ASTree.ASTChangeInformation;
import ASTree.ASTreeManipulationMethods;
import ASTree.CompilationUnitHistoryRecord;

public class ASTChangeInformationDeleteStaticMember extends ASTChangeInformation{

	String staticFieldDeclaration;
	int staticFieldDeclarationIndex;
	
	public ASTChangeInformationDeleteStaticMember(
			CompilationUnitHistoryRecord or, ASTNode node1,
			CompilationUnitHistoryRecord nr, ASTNode node2) {
		super(or, node1, nr, node2);
		staticFieldDeclaration = MoveStaticMember.getAddedStaticDeclaration(node2, node1);
		staticFieldDeclarationIndex = MoveStaticMember.getAddedStaticDeclarationIndex(node1, staticFieldDeclaration);
	
		System.out.print(staticFieldDeclaration);
	}
	
	public String getStaticFieldDeclaration()
	{
		return staticFieldDeclaration;
	}
	
	public IMember getMovedStaticField() throws Exception
	{
		CompilationUnit tree = getOldCompilationUnitRecord().getASTree();
		ASTNode node = ASTreeManipulationMethods.getASTNodeByIndex(tree, staticFieldDeclarationIndex);
		int position = node.getStartPosition();
		ICompilationUnit unit = getICompilationUnit();
		IType type = (IType)unit.getElementAt(position);
		IField[] fields = type.getFields();
		for(IField field : fields)
		{
			String s = field.getSource();
			if(staticFieldDeclaration.startsWith(s))
				return field;
		}
		return null;
	}

}
