package Rename;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;

import ASTree.ASTChangeGenerator;
import ASTree.CUHistory.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;

public class NameChangeDetector {

	public static final int MAXIMUM_LOOK_BACK_COUNT_RENAME = 5;
	
	public boolean isRenameDetected(List<CompilationUnitHistoryRecord> records) throws Exception
	{
		CompilationUnitHistoryRecord latestRecord = getLatestRecord(records);
		CompilationUnitHistoryRecord oldRecord;
		int lookBackCount = getLookingBackCount(records);
		for (int i = 1; i <= lookBackCount; i++) {
			int index = records.size() - 1 - i;
			oldRecord = records.get(index);
			ASTChangeName change = ASTChangeGenerator
					.getRenameASTChangedInformation(oldRecord, latestRecord);
			if(change != null && NameChangeDetected.getInstance().isNewChange(change))
			{
				NameChangeDetected.getInstance().addNameChange(change);
				return true;
			}
		}
		return false;
	}
	
	
	public JavaRefactoring getRefactoring(ICompilationUnit unit) throws Exception
	{
		return NameChangeDetected.getInstance().
				getLatestDetectedChange().getRenameRefactoring(unit);
	}
	
	private CompilationUnitHistoryRecord getLatestRecord(List<CompilationUnitHistoryRecord> records) 
	{
		if(records.size() < 1)
			return null;
		CompilationUnitHistoryRecord latestRecord = records
				.get(records.size() - 1);
		return latestRecord;
	}
	
	private int getLookingBackCount(List<CompilationUnitHistoryRecord> records)
	{
		if (records.size() <= 1)
			return 0;
		return Math.min(records.size() - 1, MAXIMUM_LOOK_BACK_COUNT_RENAME);
	}
	
	
	
}
