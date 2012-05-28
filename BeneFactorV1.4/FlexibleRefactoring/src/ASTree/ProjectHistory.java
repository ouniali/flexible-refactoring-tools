package ASTree;
import java.util.*;
import java.util.Map.Entry;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.*;

import util.ASTUtil;

import ASTree.CUHistory.CompilationUnitHistory;
import Rename.*;

public class ProjectHistory {
	
	
	ArrayList<CompilationUnitHistory> histories;
	CompilationUnitHistory mostRecentHistory;
	String ProjectName;
	IJavaProject project;
	

	public ProjectHistory(IJavaProject proj, String pro) {
		project = proj;
		ProjectName = pro;
		histories = new ArrayList<CompilationUnitHistory>();
	}


	public void addAST(CompilationUnit tree) throws Exception
	{
		String pacName = ASTUtil.getPackageName(tree.getPackage());
		String unitName = ASTUtil.getCompilationUnitName(tree);
		CompilationUnitHistory history = getHistory(pacName, unitName);
		
		if(history == null)
		{
			history = new CompilationUnitHistory(project, (ICompilationUnit)tree.getJavaElement(), ProjectName ,pacName, unitName );
			histories.add(history);
		}
		
		history.addAST(tree);
		mostRecentHistory = history;
	}
	
	public ASTChange getMostRecentChange()
	{
		if(mostRecentHistory == null)
			return null;
		else
			return mostRecentHistory.getLatestChange();
	}
	
	private CompilationUnitHistory getHistory(String pacName, String unitName)
	{
		for (CompilationUnitHistory his: histories)
		{
			if(his.getCompilationUnitName().equals(unitName) && his.getPackageName().equals(pacName))
				return his;
		}
		return null;
	}
	
	
}
