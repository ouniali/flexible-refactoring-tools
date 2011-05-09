package ASTree;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;

import Rename.*;

public class ProjectHistory {
	
	private ArrayList<CompilationUnit> trees;
	private ArrayList<Date> timeStamps;
	private static final int MAXIMUM_LOOK_BACK_COUNT = 10;
	
	private ArrayList<ASTNameChangeInformation> detectedNameChanges = new ArrayList<ASTNameChangeInformation>();
	private NameChangeCountHistory nameChangeHistory = new NameChangeCountHistory();
	
	public ProjectHistory()
	{
		trees = new ArrayList<CompilationUnit>();
		timeStamps = new ArrayList<Date>();
	}
	public void addAST(CompilationUnit tree)
	{
		timeStamps.add(Calendar.getInstance().getTime());
		trees.add(tree);
		
		if(LookingBackForDetectingRenameChange())
		{
			ASTNameChangeInformation infor = detectedNameChanges.get(detectedNameChanges.size()-1);
			IBinding bind = infor.getBindingOfOldName();
			int bindingCount = infor.getOldNameBindingCount();
			nameChangeHistory.addNameChange(bind, bindingCount);
			
		}
	}
	public ASTChangeInformation getMostRecentASTGeneralChange()
	{
		CompilationUnit newAST = trees.get(trees.size()-1);
		CompilationUnit oldAST = null;
		
		ArrayList<CompilationUnit> history = getASTHistory(newAST);
		if(history.size()<=1)
			return null;
		oldAST = history.get(history.size()-2);
		ASTChangeInformation change = ASTree.getGeneralASTChangeInformation(oldAST, newAST);
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
	

	private boolean LookingBackForDetectingRenameChange()
	{
		if(trees.size() == 0)
			return false;
		CompilationUnit latest = trees.get(trees.size()-1);
		ArrayList<CompilationUnit> history = getASTHistory(latest);
		
		if(history.size()<=1)
			return false;
		
		int lookBackCount = Math.min(history.size()-1, MAXIMUM_LOOK_BACK_COUNT);
		CompilationUnit oldUnit;
		
		for(int i = 1; i<= lookBackCount; i++)
		{
			int index = history.size()-1-i;
			oldUnit = trees.get(index);	
			ASTNameChangeInformation change = ASTree.getRenameASTChangedInformation(oldUnit,latest);
			if(change.getNameChangeType() != NameChange.NOT_NAME_CHANGE)
			{
				if(!detectedNameChanges.contains(change))				
				{	
					detectedNameChanges.add(change);
					return true;
				}
			}
		}
		
		return false;	
	}
	
	

	
}
