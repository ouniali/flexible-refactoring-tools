package ASTree;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.eclipse.jdt.core.dom.CompilationUnit;
import Rename.*;

public class ProjectHistory {
	
	private ArrayList<CompilationUnit> trees;
	private ArrayList<Date> timeStamps;
	
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
		String newASTPath;
		String oldASTPath;
		
		CompilationUnit newAST = trees.get(trees.size()-1);
		CompilationUnit oldAST = null;
		
		newASTPath = newAST.getJavaElement().getPath().toString();
		for(int i = trees.size()-2; i>=0; i--)
		{
			oldASTPath = trees.get(i).getJavaElement().getPath().toString();
			if(oldASTPath.equals(newASTPath))
			{	
				oldAST = trees.get(i);
				break;	
			}
		}
		if(oldAST == null)
			return null;
		ASTChangeInformation change = ASTree.getChangedASTInformation(oldAST, newAST);
		return change;
	}


	
}
