package ASTree;
import java.util.*;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.*;

import Rename.*;

public class ProjectHistory {
	
	
	ArrayList<ProjectHistoryRecord> Records;
	private static final int MAXIMUM_LOOK_BACK_COUNT = 10;
	
	private ArrayList<ASTNameChangeInformation> detectedNameChanges = new ArrayList<ASTNameChangeInformation>();
	private NameChangeCountHistory nameChangeHistory = new NameChangeCountHistory();
	
	public ProjectHistory()
	{
		Records = new ArrayList<ProjectHistoryRecord>();
	
	}

	public boolean addAST(CompilationUnit tree)
	{
		if(Records.size()>0)
		{
			CompilationUnit lastUnit = Records.get(Records.size()-1).unit;
			if(tree.subtreeMatch(new ASTMatcher(), lastUnit))
				return false;
		}
		
		Records.add(new ProjectHistoryRecord(System.currentTimeMillis(), tree));
		
		if(LookingBackForDetectingRenameChange())
		{
			ASTNameChangeInformation infor = detectedNameChanges.get(detectedNameChanges.size()-1);
			System.out.println(infor.getNameChangeTypeDescription());			
		}
		
		return true;
	}
	public ASTChangeInformation getMostRecentASTGeneralChange()
	{
		ProjectHistoryRecord newRecord = Records.get(Records.size()-1);
		ProjectHistoryRecord oldRecord = null;
		
		ArrayList<ProjectHistoryRecord> history = getASTHistory(newRecord);
		if(history.size()<=1)
			return null;
		oldRecord = history.get(history.size()-2);
		ASTChangeInformation change = ASTree.getGeneralASTChangeInformation(oldRecord.unit,oldRecord.time, newRecord.unit, newRecord.time);
		return change;
	}
	
	private ArrayList<ProjectHistoryRecord> getASTHistory(ProjectHistoryRecord rec)
	{
		ArrayList<ProjectHistoryRecord> history = new ArrayList<ProjectHistoryRecord>();
		String unitPath = rec.unit.getJavaElement().getPath().toString();
		
		for(ProjectHistoryRecord record: Records)
		{
			String currentPath = record.unit.getJavaElement().getPath().toString();
			if(unitPath.equals(currentPath))
				history.add(record);
		}
		return history;
	}
	
	private boolean LookingBackForDetectingRenameChange()
	{
		if(Records.size() == 0)
			return false;
		ProjectHistoryRecord latestRecord = Records.get(Records.size()-1);
		ArrayList<ProjectHistoryRecord> history = getASTHistory(latestRecord);
		
		if(history.size()<=1)
			return false;
		
		int lookBackCount = Math.min(history.size()-1, MAXIMUM_LOOK_BACK_COUNT);
		ProjectHistoryRecord oldRecord;
		
		for(int i = 1; i<= lookBackCount; i++)
		{
			int index = history.size()-1-i;
			oldRecord = Records.get(index);	
			ASTNameChangeInformation change = ASTree.getRenameASTChangedInformation(oldRecord.unit,oldRecord.time,latestRecord.unit, latestRecord.time);
			if(change.getNameChangeType() != NameChange.NOT_NAME_CHANGE)
			{
				if(!detectedNameChanges.contains(change))				
				{	
					IBinding bind = change.getBindingOfOldName();
					int bindingCount = change.getOldNameBindingCount();
					nameChangeHistory.addNameChange(bind, bindingCount);
					float per = nameChangeHistory.getNameChangeFraction(bind);
					change.setNameChangePercentage(per);					
					detectedNameChanges.add(change);
					return true;
				}
			}
		}
		
		return false;	
	}
	
	

	
}
