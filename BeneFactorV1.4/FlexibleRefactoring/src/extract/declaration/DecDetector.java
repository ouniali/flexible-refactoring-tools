package extract.declaration;

import java.util.List;

import compare.SourceDiff;
import compare.SourceDiffChange;
import compare.SourceDiffIdentical;
import compare.SourceDiffInsert;

import ASTree.CUHistory.CompilationUnitHistoryRecord;

public abstract class DecDetector {

	static int LOOK_BACK_COUNT = 5;
	private Declaration dec;
	
	private final int getBackCount(List<CompilationUnitHistoryRecord> records)
	{
		return Math.min(LOOK_BACK_COUNT, records.size());
	}
	
	public final boolean isDecDetected(List<CompilationUnitHistoryRecord> records)
	{
		int total = getBackCount(records);
		for(int i = records.size() - 1; i > records.size() - 1 - total; i--)
		{
			CompilationUnitHistoryRecord current = records.get(i);
			if(isDecFoundIn(current))
			{
				dec = getDeclaration(current);
				return true;
			}
		}
		return false;
	}
	
	public Declaration getDetectedDec()
	{
		return dec;
	}
	
	protected final String getEditedLineText(CompilationUnitHistoryRecord record)
	{
		SourceDiff diff = record.getSourceDiff();
		return getCode(diff);
	}
	
	private String getCode(SourceDiff diff)
	{
		if (diff instanceof SourceDiffChange) {
			return ((SourceDiffChange) diff).getCodeAfterChange();
		}
		else if(diff instanceof SourceDiffInsert){
			return ((SourceDiffInsert) diff).getInsertedCode();
		}
		else
			return "";
	}
	

	
	protected abstract boolean isDecFoundIn(CompilationUnitHistoryRecord record);
	protected abstract Declaration getDeclaration(CompilationUnitHistoryRecord current);
}
