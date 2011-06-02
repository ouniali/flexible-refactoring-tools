package ASTree;

import java.util.*;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.*;

import JavaRefactoringAPI.JavaRefactoring;
import Rename.ASTNameChangeInformation;
import Rename.NameChange;
import Rename.NameChangeCountHistory;

public class CompilationUnitHistory {
	
	String ProjectName;
	String PackageName;
	String UnitName;
	IJavaProject Project;
	ICompilationUnit unit;
	ArrayList<CompilationUnitHistoryRecord> Records;
	static final int MAXIMUM_LOOK_BACK_COUNT = 10;
	
	static private ArrayList<ASTNameChangeInformation> detectedNameChanges = new ArrayList<ASTNameChangeInformation>();
	static private NameChangeCountHistory nameChangeHistory = new NameChangeCountHistory();
	
	protected CompilationUnitHistory(IJavaProject proj, ICompilationUnit u, String pro, String pac, String un)
	{
		unit = u;
		Project = proj;
		ProjectName = pro;
		PackageName = pac;
		UnitName = un;
		Records = new ArrayList<CompilationUnitHistoryRecord>();
	}
	
	
	protected boolean addAST(CompilationUnit tree) throws Exception
	{
		if(Records.size()>0)
		{
			CompilationUnit lastUnit = Records.get(Records.size()-1).getASTree();
			if(tree.subtreeMatch(new ASTMatcher(), lastUnit))
				return false;
		}
		
		Records.add(new CompilationUnitHistoryRecord(Project, unit ,ProjectName, PackageName, UnitName, tree, System.currentTimeMillis()));
		
		if(LookingBackForDetectingRenameChange())
		{
			ASTNameChangeInformation infor = detectedNameChanges.get(detectedNameChanges.size()-1);
			JavaRefactoring.UnhandledRefactorings.add(infor.getRenameRefactoring());
			System.out.println(infor.getNameChangeTypeDescription());			
		}
		
		return true;
	}
	
	private boolean LookingBackForDetectingRenameChange() throws Exception
	{
		if(Records.size() == 0)
			return false;
		CompilationUnitHistoryRecord latestRecord = Records.get(Records.size()-1);
		
		if(Records.size()<=1)
			return false;
		
		int lookBackCount = Math.min(Records.size()-1, MAXIMUM_LOOK_BACK_COUNT);
		CompilationUnitHistoryRecord oldRecord;
		
		for(int i = 1; i<= lookBackCount; i++)
		{
			int index = Records.size()-1-i;
			oldRecord = Records.get(index);	
			ASTNameChangeInformation change = ASTreeManipulationMethods.getRenameASTChangedInformation(oldRecord,latestRecord);
			if(change != null)
			{
				if(!detectedNameChanges.contains(change))				
				{	
					String binding = change.getOldRootBindingKey();
					int bindingCount = change.getOldNameBindingCount();
					nameChangeHistory.addNameChange(binding, bindingCount);
					float per = nameChangeHistory.getNameChangeFraction(binding);
					change.setNameChangePercentage(per);					
					detectedNameChanges.add(change);
					return true;
				}
			}
		}
		
		return false;	
	}
	
	protected ASTChangeInformation getMostRecentASTGeneralChange()
	{
		CompilationUnitHistoryRecord newRecord = Records.get(Records.size()-1);
		CompilationUnitHistoryRecord oldRecord = null;
		
		if(Records.size()<=1)
			return null;
		oldRecord = Records.get(Records.size()-2);
		ASTChangeInformation change = ASTreeManipulationMethods.getGeneralASTChangeInformation(oldRecord, newRecord);
		return change;
	}
	protected String getCompilationUnitName()
	{
		return UnitName;
	}
	
	protected String getPackageName()
	{
		return PackageName;
	}

	
}
