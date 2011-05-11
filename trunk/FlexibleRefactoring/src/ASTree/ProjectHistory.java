package ASTree;
import java.util.*;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.*;

import Rename.*;

public class ProjectHistory {
	
	
	ArrayList<CompilationUnitHistory> histories;
	CompilationUnitHistory mostRecentHistory;
	
	public ProjectHistory()
	{
		histories = new ArrayList<CompilationUnitHistory>();
	}

	public boolean addAST(CompilationUnit tree) throws Exception
	{
		CompilationUnitHistory history = getHistory(tree);
		if(history == null)
		{
			history = new CompilationUnitHistory(tree.getJavaElement().getPath().toString());
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
	
	private CompilationUnitHistory getHistory(CompilationUnit unit)
	{
		String path = unit.getJavaElement().getPath().toString();
		
		for (CompilationUnitHistory his: histories)
		{
			if(his.getCompilationUnitPath().equals(path))
				return his;
		}
		
		return null;
	}
}
