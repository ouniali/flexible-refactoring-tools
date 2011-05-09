package ASTree;

import java.util.ArrayList;
import org.eclipse.jdt.core.dom.*;

import Rename.*;

public class ASTChangeInformation {

	ASTNode rootOne;
	ASTNode rootTwo;
		
	boolean rootTypeChanged;

	//name change section
	int nameChangeType;
	String originalName;
	int originalNameBindingCount;
	String newName;
	static NameChangeCountHistory nameChangeHistory = new NameChangeCountHistory();
	float nameChangeFraction;
	//name change section ends
	
	public ASTChangeInformation(ASTNode r1, ASTNode r2)
	{
		rootOne = r1;
		rootTwo = r2;
		if(rootOne.getNodeType() == rootTwo.getNodeType())
			rootTypeChanged = false;
		else	
			rootTypeChanged = true;
		
		//set name change related information
		nameChangeType = NameChange.DecideNameChangeType(rootOne, rootTwo);//if the change is modifying a name, get the type of such change.
		if(nameChangeType != NameChange.NOT_NAME_CHANGE)
		{
			originalName = NameChange.getOriginalNameAndNewName(rootOne, rootTwo)[0];
			newName = NameChange.getOriginalNameAndNewName(rootOne, rootTwo)[1];
			ArrayList<SimpleName> nodesWithSameBinding = NameChange.getNodesWithSameBinding(rootOne);
			if(nodesWithSameBinding == null)
				originalNameBindingCount = -1;
			else
				originalNameBindingCount = nodesWithSameBinding.size();
			IBinding binding = ((SimpleName)rootOne).resolveBinding();
			if( binding != null)
			{	
				nameChangeHistory.addNameChange(binding, originalNameBindingCount);
				nameChangeFraction = nameChangeHistory.getNameChangeFraction(binding);
			}
		}
		//end the setting of name change related information
	}
	public ASTNode getOldRoot()
	{
		return rootOne;
	}
	public ASTNode getNewRoot()
	{
		return rootTwo;
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
			return type+" "+originalName+" "+ originalNameBindingCount +" "+newName;
	}
	public String getChangeInformation()
	{
		StringBuffer Information = new StringBuffer();
		Information.append("Old Version:\r\n");
		Information.append(getOldRoot());
		Information.append("\r\n");
		Information.append("New Version:\r\n");
		Information.append(getNewRoot());		
		return Information.toString();
	}
	
}
