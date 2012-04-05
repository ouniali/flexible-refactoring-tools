package ASTree;
import java.util.*;
import java.util.Map.Entry;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.*;

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


	public boolean addAST(CompilationUnit tree) throws Exception
	{
		String pacName = ASTreeManipulationMethods.getPackageName(tree.getPackage());
		String unitName = ASTreeManipulationMethods.getCompilationUnitName(tree);
		
		CompilationUnitHistory history = getHistory(pacName, unitName);
		
		if(history == null)
		{
			history = new CompilationUnitHistory(project, (ICompilationUnit)tree.getJavaElement(), ProjectName ,pacName, unitName );
			histories.add(history);
		}
		
		if(history.addAST(tree))
		{
			mostRecentHistory = history;
			return true;
		}
		else
			return false;
	}
	
	public ASTChangeInformation getMostRecentChange()
	{
		if(mostRecentHistory == null)
			return null;
		else
			return mostRecentHistory.getMostRecentASTGeneralChange();
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
