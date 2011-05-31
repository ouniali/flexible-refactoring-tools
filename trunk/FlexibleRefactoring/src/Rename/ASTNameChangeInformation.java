package Rename;
import java.util.ArrayList;


import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;


import ASTree.*;
import JavaRefactoringAPI.JavaRenameRefactoringAPI;

public class ASTNameChangeInformation extends ASTChangeInformation {

	int nameChangeType;
	
	String originalName;
	String modifiedName;
	
	int originalNameBindingCount;
	
	float percentage;
	static boolean allowFinishingRenamingAutomatically = true;
	String bindingKeyOne;
	String bindingKeyTwo;
	
	
	public ASTNameChangeInformation(CompilationUnitHistoryRecord oldRecord ,ASTNode r1, CompilationUnitHistoryRecord newRecord ,ASTNode r2) throws Exception {
		super(oldRecord,r1,newRecord,r2);
		// TODO Auto-generated constructor stub
				
		ASTNode rootOne = this.getOldRoot();
		ASTNode rootTwo = this.getNewRoot();
		
		nameChangeType = NameChange.DecideNameChangeType(rootOne, rootTwo);//if the change is modifying a name, get the type of such change.
		Name oldName = (Name) rootOne;
		Name newName = (Name) rootTwo;
		
		bindingKeyOne = oldRecord.getBindingKey(oldName.getFullyQualifiedName());
		bindingKeyTwo = newRecord.getBindingKey(newName.getFullyQualifiedName());
		
		if(nameChangeType != NameChange.NOT_NAME_CHANGE)
		{
			originalName = oldName.getFullyQualifiedName();
			modifiedName = newName.getFullyQualifiedName();
			originalNameBindingCount = oldRecord.getNumberOfSameBindingInHistory(bindingKeyOne);
		}
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
		//NameChangeAutomatically();
	}
	
	public boolean NameChangeAutomatically()
	{
		SimpleName name = (SimpleName)getNewRoot();
		IBinding binding = name.resolveBinding();
		IJavaElement element = binding.getJavaElement();
		try {
			
			JavaRenameRefactoringAPI.performRefactoring(element, "newName");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;
	}
	public String getOldRootBindingKey()
	{
		return bindingKeyOne;
	}
	
	public String getNewRootBindingKey()
	{
		return bindingKeyTwo;
	}
	
	

}
