package ASTree;

import java.io.IOException;
import java.util.HashMap;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.CompilationUnit;

import Rename.ASTNameChangeInformation;

public class ProjectHistoryCollector {
	
	private HashMap<IJavaProject,ProjectHistory> Map;
	
	public int getProjectCount()
	{
		return Map.size();
	}
	
	public ProjectHistory getProjectHistory(IJavaProject pro)
	{
		return Map.get(pro);
	}
	
	public ProjectHistoryCollector()
	{
		Map = new HashMap<IJavaProject, ProjectHistory>();
	}
	
	public void addNewProjectVersion(IJavaProject project, CompilationUnit tree) throws Exception
	{

		
		ProjectHistory history = Map.get(project);
		
		if(history == null)
		{
			ProjectHistory newHistory = new ProjectHistory(project ,ASTreeManipulationMethods.getJavaProjectName(project));
			newHistory.addAST(tree);
			Map.put(project, newHistory);
		}
		else
		{	
			if(history.addAST(tree))
			{
				ASTChangeInformation GeneralChange = history.getMostRecentChange();
				ASTFileSaver.saveConciseAST(GeneralChange);
			}
		}
	}
	
}
