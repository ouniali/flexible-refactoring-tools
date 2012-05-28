package ASTree;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.ASTNode;

import ASTree.CUHistory.CompilationUnitHistoryRecord;

import compare.SourceDiffIdentical;

import util.ASTUtil;
import util.ICompilationUnitUtil;
import util.FileUtil;

public class ASTChange {

	private final IJavaProject project;
	private final ICompilationUnit unit;
	
	private final int nodeOneIndex;
	private final int nodeTwoIndex;
	
	private final CompilationUnitHistoryRecord oldRecord;
	private final CompilationUnitHistoryRecord newRecord;
	
	private final long oldTime;
	private final long newTime;

	
	static private final String root = "AST_CONCISE";
	private final String Directory;
	private final String ChangeFileName;


	
	protected ASTChange(CompilationUnitHistoryRecord or, ASTNode node1 ,CompilationUnitHistoryRecord nr, ASTNode node2)
	{
		oldRecord = or; 
		newRecord = nr;
		project = oldRecord.getIJavaProject();
		unit = oldRecord.getICompilationUnit();
		oldTime = oldRecord.getTime();
		newTime = newRecord.getTime();
		nodeOneIndex = ASTUtil.getASTNodeIndexInCompilationUnit(node1);
		nodeTwoIndex = ASTUtil.getASTNodeIndexInCompilationUnit(node2);
		Directory = root+File.separator+project.getElementName();
		ChangeFileName = oldRecord.getPackageName()+"_"+oldRecord.getCompilationUnitName()+"_"+oldRecord.getTime()+"_"+newRecord.getTime()+".txt";
		new File(Directory).mkdirs();
		FileUtil.save(getChangeFilePath(), getChangeInformation(node1, node2));
	}

	private String getChangeFilePath() {
		return Directory + File.separator + ChangeFileName;
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
		ASTChange info = (ASTChange) o;
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
		ICompilationUnitUtil.UpdateICompilationUnitWithoutCommit(unit, code, monitor);
	}
	
	public void recoverICompilationUnitToNewRecord(IProgressMonitor monitor)
	{
		String code = getNewCompilationUnitRecord().getSourceCode();
		ICompilationUnitUtil.UpdateICompilationUnitWithoutCommit(unit, code,monitor);
	}
	
	protected int getRefactoringMarkerLine(ICompilationUnit unit) throws Exception
	{
		CompilationUnitHistoryRecord current = getNewCompilationUnitRecord();
		CompilationUnitHistoryRecord old = getOldCompilationUnitRecord();
		for(;!current.equals(old);current = current.getPreviousRecord())
		{
			if(current.hasMeaningfulChangedLineNumber())
				return current.getChangedLineNumberFromPrevious();
		}
		return 0;
	}
	
	public static String getChangeFilesRoot()
	{
		return root;
	}

}
