package Rename;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.*;

import userinterface.RefactoringMarker;
import ASTree.*;
import JavaRefactoringAPI.*;

public class ASTNameChangeInformation extends ASTChangeInformation {

	int nameChangeType;
	
	String originalName;
	String originalNameFull;
	String modifiedName;
	String modifiedNameFull;
	
//	int originalNameBindingCount;
	
//	float percentage;
//	final float PERCENTAGE_THRESHHOLD = (float)0.5;
	static boolean allowFinishingRenamingAutomatically = true;
	String bindingKeyOne;
	String bindingKeyTwo;
	
	int oldNameNodeIndex;
	int newNameNodeIndex;
	
	boolean isDeclarationChange;
	int declarationNodeIndex;
	
	
	public ASTNameChangeInformation(CompilationUnitHistoryRecord oldRecord ,ASTNode r1, CompilationUnitHistoryRecord newRecord ,ASTNode r2) throws Exception {
		
		super(oldRecord,r1,newRecord,r2);
		
		Name oldName = (Name) r1;
		Name newName = (Name) r2;
		
		bindingKeyOne = oldRecord.getBindingKey(oldName.getFullyQualifiedName());
		bindingKeyTwo = newRecord.getBindingKey(newName.getFullyQualifiedName());
		
		originalName = oldName.toString();
		originalNameFull = oldName.getFullyQualifiedName();
		modifiedName = newName.toString();
		modifiedNameFull = newName.getFullyQualifiedName();
		
//		originalNameBindingCount = oldRecord.getNumberOfSameBindingInHistory(bindingKeyOne);
	
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
		//return originalNameBindingCount;
		return 0;
	}
	
	public float getNameChangePercentage()
	{
		//return percentage;
		return (float) 0.0;
	}
	
	public void setNameChangePercentage(float per)
	{
		//percentage = per;
	}
	

	public String getOldNameBindingKey()
	{
		return bindingKeyOne;
	}
	
	public String getNewNameBindingKey()
	{
		return bindingKeyTwo;
	}
	
	public JavaRefactoring getRenameRefactoring(ICompilationUnit unit) throws Exception
	{
		int line = getRefactoringMarkerLine(unit);
		IMarker marker = RefactoringMarker.addRefactoringMarkerIfNo(unit, line);
		boolean usingDiff1 = true;
		boolean usingDiff2 = false;
		
		if(bindingKeyOne.equals("") && !bindingKeyTwo.equals(""))
		{
			ASTNameChangeInformation declarationChange = NameChange.searchDeclarationChangeInHistory(bindingKeyTwo);
			if(declarationChange == null)
				return null;
			
			String keyBefore = declarationChange.getOldNameBindingKey();
			String keyAfter = declarationChange.getNewNameBindingKey();
			if(!keyBefore.equals("") && !keyAfter.equals(""))
			{
				if(usingDiff1)
				{
					JavaRefactoringRenameDiff refactoringDiff = new JavaRefactoringRenameDiff(
							unit,
							line,
							marker,
							declarationChange,
							modifiedName
							);
					return refactoringDiff;
				}
				else
				{
					JavaRefactoringRename refactoring = new JavaRefactoringRename(
							unit,
							line,
							marker,
							keyBefore,
							keyAfter,
							originalName,
							modifiedName
							);
					return refactoring;
				}
			}
			else
				return null;
		}
		else
		{
			if(this.isRenamingDeclaration())
			{
				if(usingDiff2)
				{
					JavaRefactoringRenameDiff refactoringDiff = new JavaRefactoringRenameDiff(
							unit,
							line,
							marker,
							this,
							modifiedName
							);					
					return refactoringDiff;
				}
				else
				{
					JavaRefactoringRename refactoring = new JavaRefactoringRename(
							unit, 
							line, 
							marker, 
							bindingKeyOne, 
							bindingKeyTwo,
							originalName, 
							modifiedName);
					return refactoring;
				}
				
			}
			else
			{
				JavaRefactoringRename refactoring = new JavaRefactoringRename(
						unit, 
						line, 
						marker, 
						bindingKeyOne, 
						bindingKeyOne,
						originalName, 
						modifiedName
						);
				return refactoring;
			}
		}	
	}
	
	public boolean isPercentageAboveThreshhold()
	{
		//return percentage> PERCENTAGE_THRESHHOLD;
		return false;
	}
	
	public boolean isRenameComplete(ICompilationUnit unit)
	{	
		//return percentage == 1.00;
		return false;
	}
	
	public boolean isRenamingDeclaration()
	{
		return isDeclarationChange;
	}
	
	public String getOldName()
	{
		return originalName;
	}
	
	public String getOldNameFull()
	{
		return originalNameFull;
	}
	
	public String getNewName()
	{
		return modifiedName;
	}
	
	public String getNewNameFull()
	{
		return modifiedNameFull;
	}
	
	public int getRefactoringMarkerLine(ICompilationUnit unit) throws Exception
	{
		CompilationUnit tree = ASTreeManipulationMethods.parseICompilationUnit(unit);
		ASTNode oldNameNode = ASTreeManipulationMethods.getASTNodeByIndex(tree, oldNameNodeIndex);
		int lineNo = tree.getLineNumber(oldNameNode.getStartPosition());
		return lineNo;
	}

}