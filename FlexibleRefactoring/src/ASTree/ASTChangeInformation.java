package ASTree;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.*;

import Rename.*;

public class ASTChangeInformation {

	IJavaProject project;
	ICompilationUnit unit;
	ASTNode rootOne;
	ASTNode rootTwo;
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
		rootOne = node1;
		rootTwo = node2;
		oldTime = oldRecord.getTime();
		newTime = newRecord.getTime();
		
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
