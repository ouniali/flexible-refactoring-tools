package ASTree;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.ASTNode;

import utitilies.FileUtilities;

public class ASTChangeInformation {

	IJavaProject project;
	ICompilationUnit unit;
	
	int nodeOneIndex;
	int nodeTwoIndex;
	
	CompilationUnitHistoryRecord oldRecord;
	CompilationUnitHistoryRecord newRecord;
	
	long oldTime;
	long newTime;

	boolean rootTypeChanged;
	
	static final String root = "AST_CONCISE";
	final String Directory;
	final String ChangeFileName;


	
	protected ASTChangeInformation(CompilationUnitHistoryRecord or, ASTNode node1 ,CompilationUnitHistoryRecord nr, ASTNode node2)
	{
		oldRecord = or; 
		newRecord = nr;
		
		project = oldRecord.getIJavaProject();
		unit = oldRecord.getICompilationUnit();
		
		oldTime = oldRecord.getTime();
		newTime = newRecord.getTime();
		
		nodeOneIndex = ASTreeManipulationMethods.getASTNodeIndexInCompilationUnit(node1);
		nodeTwoIndex = ASTreeManipulationMethods.getASTNodeIndexInCompilationUnit(node2);
		
		if(node1.getNodeType() == node2.getNodeType())
			rootTypeChanged = false;
		else	
			rootTypeChanged = true;
		
		Directory = root+File.separator+project.getElementName();
		ChangeFileName = oldRecord.getPackageName()+"_"+oldRecord.getCompilationUnitName()+"_"+oldRecord.getTime()+"_"+newRecord.getTime()+".txt";
		new File(Directory).mkdirs();
		FileUtilities.save(Directory + File.separator + ChangeFileName, getChangeInformation(node1, node2));
	}
	

	
	public String getChangeInformation(ASTNode node1, ASTNode node2)
	{
		StringBuffer Information = new StringBuffer();
		Information.append("Old Version:\r\n");
		Information.append(node1);
		Information.append("\r\n");
		Information.append("New Version:\r\n");
		Information.append(node2);		
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
	public int getNodeOneIndex()
	{
		return nodeOneIndex;
	}
	public int getNodeTwoIndex()
	{
		return nodeTwoIndex;
	}
	
	public void recoverICompilationUnitToOldRecord(IProgressMonitor monitor)
	{
		String code = getOldCompilationUnitRecord().getSourceCode();
		CompilationUnitManipulationMethod.UpdateICompilationUnitWithoutCommit(unit, code, monitor);
		//CompilationUnitManipulationMethod.FormattICompilationUnit(unit);
	}
	
	public void recoverICompilationUnitToNewRecord(IProgressMonitor monitor)
	{
		String code = getNewCompilationUnitRecord().getSourceCode();
		CompilationUnitManipulationMethod.UpdateICompilationUnitWithoutCommit(unit, code,monitor);
		//CompilationUnitManipulationMethod.FormattICompilationUnit(unit);
	}
	public int getRefactoringMarkerLine(ICompilationUnit unit) throws Exception
	{
		return 0;
	}

}
