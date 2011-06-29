package Rename;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.*;

import userinterface.RefactoringMarker;
import ASTree.*;
import JavaRefactoringAPI.JavaRefactoringType;
import JavaRefactoringAPI.*;

public class ASTNameChangeInformation extends ASTChangeInformation {

	int nameChangeType;
	
	String originalName;
	String modifiedName;
	
	int originalNameBindingCount;
	
	float percentage;
	final float PERCENTAGE_THRESHHOLD = (float)0.5;
	static boolean allowFinishingRenamingAutomatically = true;
	String bindingKeyOne;
	String bindingKeyTwo;
	
	int oldNameNodeIndex;
	int newNameNodeIndex;
	
	boolean isDeclarationChange;
	int declarationNodeIndex;
	
	
	public ASTNameChangeInformation(CompilationUnitHistoryRecord oldRecord ,ASTNode r1, CompilationUnitHistoryRecord newRecord ,ASTNode r2) throws Exception {
		
		super(oldRecord,r1,newRecord,r2);
		
		nameChangeType = NameChange.DecideNameChangeType(r1, r2);
		Name oldName = (Name) r1;
		Name newName = (Name) r2;
		
		bindingKeyOne = oldRecord.getBindingKey(oldName.getFullyQualifiedName());
		bindingKeyTwo = newRecord.getBindingKey(newName.getFullyQualifiedName());
		
		if(nameChangeType != NameChange.NOT_NAME_CHANGE)
		{
			originalName = oldName.getFullyQualifiedName();
			modifiedName = newName.getFullyQualifiedName();
			originalNameBindingCount = oldRecord.getNumberOfSameBindingInHistory(bindingKeyOne);
		}
		oldNameNodeIndex = this.getNodeOneIndex();
		newNameNodeIndex = this.getNodeTwoIndex();
		
		if(oldName instanceof SimpleName)
		{
			SimpleName sOldName = (SimpleName) oldName; 
			isDeclarationChange = sOldName.isDeclaration();
		}
		else isDeclarationChange = false;
	}
	
	public int getNameChangeType()
	{
		return nameChangeType;
	}
	
	public String getNameChangeTypeDescription()
	{
		String type = NameChange.getNameChangeTypeDescription(nameChangeType);
		if(nameChangeType == NameChange.NOT_NAME_CHANGE)
			return type;
		else
			return originalName+" "+ getNameChangePercentage() + " "+modifiedName;
	}
	


	public int getOldNameBindingCount()
	{
		return originalNameBindingCount;
	}
	
	public float getNameChangePercentage()
	{
		return percentage;
	}
	
	public void setNameChangePercentage(float per)
	{
		percentage = per;
	}
	

	public String getOldNameBindingKey()
	{
		return bindingKeyOne;
	}
	
	public String getNewNameBindingKey()
	{
		return bindingKeyTwo;
	}
	
	public JavaRefactoringRename getRenameRefactoring()
	{
		String tempKey = bindingKeyOne;
		boolean fakeBinding = false;
		if(tempKey == "")
		{
			tempKey = NameChange.searchBindingKeyInHistory(originalName);
			fakeBinding = true;
		}
		JavaRefactoringRename refactoring = new JavaRefactoringRename(tempKey, modifiedName, fakeBinding);
		return refactoring;
	}
	
	public boolean isPercentageAboveThreshhold()
	{
		return percentage> PERCENTAGE_THRESHHOLD;
	}
	
	public boolean isRenameComplete()
	{
		return percentage == 1.00;
	}
	
	public boolean isRenamingDeclaration()
	{
		return isDeclarationChange;
	}
	public String getOldName()
	{
		return originalName;
	}
	public String getNewName()
	{
		return modifiedName;
	}
	public void addRefactoringMarker(ICompilationUnit unit) throws Exception
	{
		CompilationUnit tree = ASTreeManipulationMethods.parseICompilationUnit(unit);
		ASTNode oldNameNode = ASTreeManipulationMethods.getASTNodeByIndex(tree, oldNameNodeIndex);
		int lineNo = tree.getLineNumber(oldNameNode.getStartPosition());
		RefactoringMarker.addAutomaticRefactoringProposal(unit, lineNo, JavaRefactoringType.RENAME);
	}

}
