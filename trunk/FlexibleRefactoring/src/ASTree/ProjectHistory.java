package ASTree;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.eclipse.jdt.core.dom.CompilationUnit;
import Rename.*;

public class ProjectHistory {
	
	private ArrayList<CompilationUnit> trees;
	private ArrayList<Date> timeStamps;
	private static final int MAXIMUM_LOOK_BACK_COUNT = 10;
	
	public ProjectHistory()
	{
		trees = new ArrayList<CompilationUnit>();
		timeStamps = new ArrayList<Date>();
	}
	public void addAST(CompilationUnit tree)
	{
		timeStamps.add(Calendar.getInstance().getTime());
		trees.add(tree);
	}
	public ASTChangeInformation getMostRecentASTChange()
	{
		CompilationUnit newAST = trees.get(trees.size()-1);
		CompilationUnit oldAST = null;
		
		ArrayList<CompilationUnit> history = getASTHistory(newAST);
		if(history.size()<=1)
			return null;
		oldAST = history.get(history.size()-2);
		ASTChangeInformation change = ASTree.getChangedASTInformation(oldAST, newAST);
		return change;
	}
	
	private ArrayList<CompilationUnit> getASTHistory(CompilationUnit unit)
	{
		ArrayList<CompilationUnit> history = new ArrayList<CompilationUnit>();
		String unitPath = unit.getJavaElement().getPath().toString();
		
		for (CompilationUnit current: trees)
		{
			String currentPath = current.getJavaElement().getPath().toString();
			if(unitPath.equals(currentPath))
				history.add(current);
		}
		
		return history;
	}
	public ASTChangeInformation LookingBackForDetectingRenameChange()
	{
		if(trees.size()<=1)
			return null;
		CompilationUnit latest = trees.get(trees.size()-1);
		ArrayList<CompilationUnit> history = getASTHistory(latest);
		
		int lookBackCount = Math.min(history.size()-1, MAXIMUM_LOOK_BACK_COUNT);
		CompilationUnit unit;
		
		for(int i = 1; i<= lookBackCount; i++)
		{
			int index = history.size()-1-i;
			unit = trees.get(index);
			ASTChangeInformation change = new ASTChangeInformation(unit,latest);
			if(change.nameChangeType != NameChange.NOT_NAME_CHANGE)
				return change;
		}
		
		return null;
	}
	
	

	
}
