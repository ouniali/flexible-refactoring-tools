package Rename;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.*;

import JavaRefactoringAPI.rename.JavaRefactoringRename;
import JavaRefactoringAPI.rename.JavaRefactoringRenameDiff;

import userinterface.RefactoringMarker;
import util.ASTUtil;
import ASTree.*;
import JavaRefactoringAPI.*;

public class ASTNameChangeInformation extends ASTChangeInformation {

	int nameChangeType;
	
	String originalName;
	String originalNameFull;
	String modifiedName;
	String modifiedNameFull;
	
	String bindingKeyOne;
	String bindingKeyTwo;
	
	int oldNameNodeIndex;
	int newNameNodeIndex;
	
	boolean isDeclarationChange;
	boolean hasIntermediateChange;
	int declarationNodeIndex;
	
	
	public ASTNameChangeInformation(CompilationUnitHistoryRecord oldRecord ,ASTNode r1, CompilationUnitHistoryRecord newRecord ,ASTNode r2) throws Exception {
		
		super(oldRecord,r1,newRecord,r2);
		
		Name oldName = (Name) r1;
		Name newName = (Name) r2;
		
		oldNameNodeIndex = this.getNodeOneIndex();
		newNameNodeIndex = this.getNodeTwoIndex();
		
		bindingKeyOne = oldRecord.getBindingKey(oldNameNodeIndex);
		bindingKeyTwo = newRecord.getBindingKey(newNameNodeIndex);
		
		originalName = oldName.toString();
		originalNameFull = oldName.getFullyQualifiedName();
		modifiedName = newName.toString();
		modifiedNameFull = newName.getFullyQualifiedName();
		
		
		
		if(oldName instanceof SimpleName)
		{
			SimpleName sOldName = (SimpleName) oldName; 
			isDeclarationChange = sOldName.isDeclaration();
			if(isDeclarationChange)
			{
				ASTNameChangeInformation change = NameChangeDetected.getInstance().searchIntermediateChange(this);
				if(change == null)
					hasIntermediateChange = false;
				else
					hasIntermediateChange = true;
			}
		}
		else 
			isDeclarationChange = false;
	}
	
	public int getNameChangeType()
	{
		return nameChangeType;
	}
	
	public String getNameChangeTypeDescription()
	{
		String type = NameChangeUtil.getNameChangeTypeDescription(nameChangeType);
		if(nameChangeType == NameChangeUtil.NOT_NAME_CHANGE)
			return type;
		else
			return originalName + ":" + modifiedName;
	}
	


	public int getOldNameBindingCount()
	{
		//return originalNameBindingCount;
		return 0;
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
		
		if(bindingKeyOne.equals("") && !bindingKeyTwo.equals(""))
		{
			//renaming reference when declaration has been changed
			List<ASTNameChangeInformation> declarationChanges = 
					NameChangeDetected.getInstance().getSkipedDeclaredNameChangesInHistory(bindingKeyTwo);
			ASTNameChangeInformation first_change = declarationChanges.get(0);
			ASTNameChangeInformation last_change = declarationChanges.get(declarationChanges.size()-1);
				
			String keyBefore = first_change.getOldNameBindingKey();
			String keyAfter = last_change.getNewNameBindingKey();
			
			if(!keyBefore.equals("") && !keyAfter.equals(""))
			{
				JavaRefactoringRenameDiff refactoringDiff = JavaRefactoringRenameDiff.create(
						unit, line, marker, declarationChanges, modifiedName);
				return refactoringDiff;		
			}
			else
				return null;
		}
		else
		{
			if(isRenamingDeclaration())
			{
				List<ASTNameChangeInformation> changes = NameChangeDetected.getInstance().
						getSkipedDeclaredNameChangesInHistory(bindingKeyTwo);			
				changes.add(this);
				JavaRefactoringRenameDiff refactoringDiff = JavaRefactoringRenameDiff.create(
					unit, line, marker, changes, modifiedName);					
				return refactoringDiff;
				
			}
			else
			{
				JavaRefactoringRename refactoring = new JavaRefactoringRename(
					unit, line, marker, bindingKeyOne, bindingKeyOne, originalName, modifiedName);
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
		CompilationUnit tree = ASTUtil.parseICompilationUnit(unit);
		ASTNode oldNameNode = ASTUtil.getASTNodeByIndex(tree, oldNameNodeIndex);
		int lineNo = tree.getLineNumber(oldNameNode.getStartPosition());
		return lineNo;
	}
	
	

}
