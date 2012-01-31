package movestaticmember;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import userinterface.RefactoringMarker;

import ASTree.ASTChangeInformation;
import ASTree.ASTreeManipulationMethods;
import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoringMoveStaticMember;

public class ASTChangeInformationAddStaticMember extends ASTChangeInformation 
{

	String staticFieldDeclaration;
	int staticFieldDeclarationIndex;
	String targetTypeFullyQualifiedName;
	
	public ASTChangeInformationAddStaticMember(
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
	
	public JavaRefactoringMoveStaticMember getMoveStaticMemberRefactoring(ICompilationUnit unit, ASTChangeInformationDeleteStaticMember deleteChange) throws Exception
	{
		int line = getRefactoringMarkerLine(unit);
		IMarker marker = RefactoringMarker.addRefactoringMarkerIfNo(unit, line);
		return new JavaRefactoringMoveStaticMember(unit, line, marker, deleteChange ,this);
	}
	public int getRefactoringMarkerLine(ICompilationUnit unit) throws Exception
	{
		CompilationUnit tree = ASTreeManipulationMethods.parseICompilationUnit(unit);
		ASTNode node = ASTreeManipulationMethods.getASTNodeByIndex(tree, staticFieldDeclarationIndex);
		int lineNo = tree.getLineNumber(node.getStartPosition());
		return lineNo;
	}
	
	public String getDestinationTypeFullName()
	{
		return targetTypeFullyQualifiedName;
	}

}
