package ASTree;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

import Rename.ASTNameChangeInformation;
import Rename.NameChange;
import Rename.NameChangeCountHistory;

public class CompilationUnitHistory {
	
	String path;
	ArrayList<CompilationUnitHistoryRecord> Records;
	static final int MAXIMUM_LOOK_BACK_COUNT = 10;
	static private ArrayList<ASTNameChangeInformation> detectedNameChanges = new ArrayList<ASTNameChangeInformation>();
	static private NameChangeCountHistory nameChangeHistory = new NameChangeCountHistory();
	
	protected CompilationUnitHistory(String p)
	{
		path = p;
		Records = new ArrayList<CompilationUnitHistoryRecord>();
	}
	
	
	protected boolean addAST(CompilationUnit tree)
	{
		if(Records.size()>0)
		{
			CompilationUnit lastUnit = Records.get(Records.size()-1).unit;
			if(tree.subtreeMatch(new ASTMatcher(), lastUnit))
				return false;
		}
		
		Records.add(new CompilationUnitHistoryRecord(System.currentTimeMillis(), tree));
		
		if(LookingBackForDetectingRenameChange())
		{
			ASTNameChangeInformation infor = detectedNameChanges.get(detectedNameChanges.size()-1);
			System.out.println(infor.getNameChangeTypeDescription());			
		}
		
		return true;
	}
	
	private boolean LookingBackForDetectingRenameChange()
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
	
	protected ASTChangeInformation getMostRecentASTGeneralChange()
	{
		CompilationUnitHistoryRecord newRecord = Records.get(Records.size()-1);
		CompilationUnitHistoryRecord oldRecord = null;
		
		if(Records.size()<=1)
			return null;
		oldRecord = Records.get(Records.size()-2);
		ASTChangeInformation change = ASTree.getGeneralASTChangeInformation(oldRecord.unit,oldRecord.time, newRecord.unit, newRecord.time);
		return change;
	}
	
	protected String getCompilationUnitPath()
	{
		return path;
	}
	
}
