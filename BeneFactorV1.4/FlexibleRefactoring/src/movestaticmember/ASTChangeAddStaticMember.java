package movestaticmember;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import JavaRefactoringAPI.move.JavaRefactoringMoveStaticMember;

import userinterface.RefactoringMarker;
import util.ASTUtil;

import ASTree.ASTChange;
import ASTree.CUHistory.CompilationUnitHistoryRecord;

public class ASTChangeAddStaticMember extends ASTChange 
{

	String staticFieldDeclaration;
	int staticFieldDeclarationIndex;
	String targetTypeFullyQualifiedName;
	
	public ASTChangeAddStaticMember(
			CompilationUnitHistoryRecord or, ASTNode node1,
			CompilationUnitHistoryRecord nr, ASTNode node2) 
	{
		super(or, node1, nr, node2);
		staticFieldDeclaration = MoveStaticMember.getAddedStaticDeclaration(node1, node2);
		staticFieldDeclarationIndex = MoveStaticMember.getAddedStaticDeclarationIndex(node2, staticFieldDeclaration);
		targetTypeFullyQualifiedName =  ((TypeDeclaration) node2).getName().getFullyQualifiedName();
		System.out.print(staticFieldDeclaration);
	}
	
	public String getStaticFieldDeclaration()
	{
		return staticFieldDeclaration;
	}
	
	public JavaRefactoringMoveStaticMember getMoveStaticMemberRefactoring(ICompilationUnit unit, ASTChangeDeleteStaticMember deleteChange) throws Exception
	{
		int line = getRefactoringMarkerLine(unit);
		return new JavaRefactoringMoveStaticMember(unit, line, deleteChange ,this);
	}
	public int getRefactoringMarkerLine(ICompilationUnit unit) throws Exception
	{
		CompilationUnit tree = ASTUtil.parseICompilationUnit(unit);
		ASTNode node = ASTUtil.getASTNodeByIndex(tree, staticFieldDeclarationIndex);
		int lineNo = tree.getLineNumber(node.getStartPosition());
		return lineNo;
	}
	
	public String getDestinationTypeFullName()
	{
		return targetTypeFullyQualifiedName;
	}

}
