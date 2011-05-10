package Rename;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.*;

import ASTree.*;

public class ASTNameChangeInformation extends ASTChangeInformation {

	int nameChangeType;
	String originalName;
	int originalNameBindingCount;
	String newName;
	float percentage;
	
	public ASTNameChangeInformation(ASTNode r1, long t1,ASTNode r2, long t2) {
		super(r1, t1 ,r2, t2);
		// TODO Auto-generated constructor stub

		ASTNode rootOne = this.getOldRoot();
		ASTNode rootTwo = this.getNewRoot();
		nameChangeType = NameChange.DecideNameChangeType(this.getOldRoot(), this.getNewRoot());//if the change is modifying a name, get the type of such change.
		if(nameChangeType != NameChange.NOT_NAME_CHANGE)
		{
			originalName = NameChange.getOriginalNameAndNewName(rootOne, rootTwo)[0];
			newName = NameChange.getOriginalNameAndNewName(rootOne, rootTwo)[1];
			ArrayList<SimpleName> nodesWithSameBinding = NameChange.getNodesWithSameBinding(rootOne);
			if(nodesWithSameBinding == null)
				originalNameBindingCount = -1;
			else
				originalNameBindingCount = nodesWithSameBinding.size();
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
			return type+" "+originalName+" "+ originalNameBindingCount + " "+newName;
	}
	
	public IBinding getBindingOfOldName()
	{
		if(nameChangeType != NameChange.NOT_NAME_CHANGE)
		{
			SimpleName name = (SimpleName)this.getOldRoot();
			IBinding bind = name.resolveBinding();
			return bind;
		}
		
		return null;
	}	
	
	public IBinding getBindingOfNewName()
	{
		if(nameChangeType != NameChange.NOT_NAME_CHANGE)
		{
			SimpleName name = (SimpleName)this.getNewRoot();
			IBinding bind = name.resolveBinding();
			return bind;
		}
		
		return null;
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

}
