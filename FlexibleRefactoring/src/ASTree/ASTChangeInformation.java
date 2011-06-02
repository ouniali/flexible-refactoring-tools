package ASTree;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.*;

import Rename.*;

public class ASTChangeInformation {

	IJavaProject project;
	ICompilationUnit unit;
	
	int nodeOneStart;
	int nodeOneLength;
	int nodeOneType;
	
	int nodeTwoStart;
	int nodeTwoLength;
	int nodeTwoType;
	
	CompilationUnitHistoryRecord oldRecord;
	CompilationUnitHistoryRecord newRecord;
	
	long oldTime;
	long newTime;

	boolean rootTypeChanged;


	
	protected ASTChangeInformation(CompilationUnitHistoryRecord or, ASTNode node1 ,CompilationUnitHistoryRecord nr, ASTNode node2)
	{
		oldRecord = or; 
		newRecord = nr;
		
		project = oldRecord.getIJavaProject();
		unit = oldRecord.getICompilationUnit();
		
		oldTime = oldRecord.getTime();
		newTime = newRecord.getTime();
		
		
		nodeOneStart = node1.getStartPosition();
		nodeOneLength = node1.getLength();
		nodeOneType = node1.getNodeType();
		
		nodeTwoStart = node2.getStartPosition();
		nodeTwoLength = node2.getLength();
		nodeTwoType = node2.getNodeType();
		
		if(node1.getNodeType() == node2.getNodeType())
			rootTypeChanged = false;
		else	
			rootTypeChanged = true;
		
	}
/*	public ASTNode getOldNode()
	{
		return rootOne;
	}
	public ASTNode getNewNode()
	{
		return rootTwo;
	}*/

	
	public String getChangeInformation()
	{
		String oldNode;
		String newNode;
		
		oldNode = oldRecord.getASTree().toString();
		oldNode = oldNode.substring(nodeOneStart, nodeOneLength + nodeOneStart-1);
		newNode = newRecord.getASTree().toString();
		newNode = newNode.substring(nodeTwoStart, nodeTwoLength + nodeTwoStart -1);
		StringBuffer Information = new StringBuffer();
		Information.append("Old Version:\r\n");
		Information.append(oldNode);
		Information.append("\r\n");
		Information.append("New Version:\r\n");
		Information.append(newNode);		
		return Information.toString();
	}
	public long getOldTime()
	{
		return oldTime;
	}
	public long getNewTime()
	{
		return newTime;
	}
	
	@Override
	public boolean equals(Object o)
	{
		ASTChangeInformation info = (ASTChangeInformation) o;
		return info.getOldTime() == this.getOldTime() && info.getNewTime() == this.getNewTime();	
	}
	
	public IJavaProject getIJavaProject()
	{
		return project;
	}
	
	public ICompilationUnit getICompilationUnit()
	{
		return unit;
	}
	
	public CompilationUnitHistoryRecord getOldCompilationUnitRecord()
	{
		return oldRecord;
	}
	
	public CompilationUnitHistoryRecord getNewCompilationUnitRecord()
	{
		return newRecord;
	}

}
