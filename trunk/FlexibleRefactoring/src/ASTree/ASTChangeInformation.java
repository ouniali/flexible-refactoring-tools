package ASTree;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.*;

import Rename.*;

public class ASTChangeInformation {

	ASTNode rootOne;
	ASTNode rootTwo;
	long oldTime;
	long newTime;
	IJavaProject project;	
	ICompilationUnit unit;
	boolean rootTypeChanged;

	//name change section

	//name change section ends
	
	protected ASTChangeInformation(IJavaProject proj, ICompilationUnit iu,ASTNode r1, long time1, ASTNode r2, long time2)
	{
		project = proj;
		unit = iu;
		rootOne = r1;
		rootTwo = r2;
		oldTime = time1;
		newTime = time2;
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
	
}
