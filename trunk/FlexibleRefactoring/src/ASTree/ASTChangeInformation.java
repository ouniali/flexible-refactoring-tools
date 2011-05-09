package ASTree;

import java.util.ArrayList;
import org.eclipse.jdt.core.dom.*;

import Rename.*;

public class ASTChangeInformation {

	ASTNode rootOne;
	ASTNode rootTwo;
		
	boolean rootTypeChanged;

	//name change section

	//name change section ends
	
	protected ASTChangeInformation(ASTNode r1, ASTNode r2)
	{
		rootOne = r1;
		rootTwo = r2;
		if(rootOne.getNodeType() == rootTwo.getNodeType())
			rootTypeChanged = false;
		else	
			rootTypeChanged = true;
		
	}
	public ASTNode getOldRoot()
	{
		return rootOne;
	}
	public ASTNode getNewRoot()
	{
		return rootTwo;
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
	
	@Override
	public boolean equals(Object o)
	{
		ASTChangeInformation info = (ASTChangeInformation) o;
		return info.getNewRoot().equals(this.getNewRoot()) && info.getOldRoot().equals(this.getOldRoot());	
	}
	
}
