package ASTree;
import java.util.*;
import java.util.Map.Entry;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.*;

import util.ASTUtil;

import ASTree.CUHistory.CUHistoryInterface;
import ASTree.CUHistory.CompilationUnitHistory;
import ASTree.CUHistory.ControlSizedCUHistory;
import Rename.*;

public class ProjectHistory {
	
	
	private final List<CUHistoryInterface> histories;
	private CUHistoryInterface mostRecentHistory;
	private final String ProjectName;
	private final IJavaProject project;
	

	public ProjectHistory(IJavaProject proj, String pro) {
		project = proj;
		ProjectName = pro;
		histories = new ArrayList<CUHistoryInterface>();
	}


	public void addAST(CompilationUnit tree) throws Exception
	{
		String pacName = ASTUtil.getPackageName(tree.getPackage());
		String unitName = ASTUtil.getCompilationUnitName(tree);
		CUHistoryInterface history = getHistory(pacName, unitName);
		
		if(history == null)
		{
			history = new ControlSizedCUHistory
					(new CompilationUnitHistory(project, (ICompilationUnit)tree.getJavaElement(), ProjectName ,pacName, unitName));
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
	
	private CUHistoryInterface getHistory(String pacName, String unitName)
	{
		for (CUHistoryInterface his: histories)
		{
			if(his.getCompilationUnitName().equals(unitName) && his.getPackageName().equals(pacName))
				return his;
		}
		return null;
	}
	
	
}
