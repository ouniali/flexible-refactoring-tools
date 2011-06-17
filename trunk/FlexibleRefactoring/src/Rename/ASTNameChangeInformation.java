package Rename;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.*;
import ASTree.*;
import JavaRefactoringAPI.JavaRenameRefactoring;

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
	
	
	public ASTNameChangeInformation(CompilationUnitHistoryRecord oldRecord ,ASTNode r1, CompilationUnitHistoryRecord newRecord ,ASTNode r2) throws Exception {
		super(oldRecord,r1,newRecord,r2);
		// TODO Auto-generated constructor stub				
		
		nameChangeType = NameChange.DecideNameChangeType(r1, r2);//if the change is modifying a name, get the type of such change.
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
	public JavaRenameRefactoring getRenameRefactoring()
	{
		JavaRenameRefactoring refactoring = new JavaRenameRefactoring(bindingKeyOne, modifiedName);
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
	
	public void addRefactoringMarker(ICompilationUnit unit)
	{
		
	}

}
