package extractlocalvariable;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;

import util.ASTUtil;

import ASTree.ASTChangeInformationGenerator;
import ASTree.CompilationUnitHistoryRecord;
import ASTree.NewRootPair;
import JavaRefactoringAPI.JavaRefactoring;

public class ExtractLocalVariableDetector {
	
	private final int LOOK_BACK_COUNT = 5;
	
	public boolean isELVFound(List<CompilationUnitHistoryRecord> records)
	{
		CompilationUnitHistoryRecord r1 = getELVRecordOrNull(records, new CutDetectStrategy());
		CompilationUnitHistoryRecord r2 = getELVRecordOrNull(records, new CopyDetectStrategy());
		return !(r1 == null && r2 == null);
	}
	
	public JavaRefactoring getELVRefactoring(List<CompilationUnitHistoryRecord> records)
	{
		CompilationUnitHistoryRecord r1 = getELVRecordOrNull(records, new CutDetectStrategy());
		CompilationUnitHistoryRecord r2 = getELVRecordOrNull(records, new CopyDetectStrategy());
		if(null != r1)
			return new ExtractLocalVariableCut(r1).getELVRefactoring();
		else
			return new ExtractLocalVariableCopy(r2).getELVRefactoring();

	}
	

	private CompilationUnitHistoryRecord getELVRecordOrNull(
			List<CompilationUnitHistoryRecord> records, DetectStrategy str)
	{
		int lookBack = Math.min(records.size() - 1, LOOK_BACK_COUNT);
		for(int i = 0; i <= lookBack; i ++)
		{
			int index = records.size() - 1 - i;
			CompilationUnitHistoryRecord current = records.get(index);
			if(str.isELVRecord(current))
				return current;
		}
		return null;
	}
}

interface DetectStrategy
{
	public boolean isELVRecord(CompilationUnitHistoryRecord record);
}

class CutDetectStrategy implements DetectStrategy
{
	public boolean isELVRecord(CompilationUnitHistoryRecord record) 
	{
		boolean cut = record.hasCutCommand();
		String exp = record.getHighlightedText();
		return cut && ASTUtil.isExpression(exp);
	}
}

class CopyDetectStrategy implements DetectStrategy
{
	public boolean isELVRecord(CompilationUnitHistoryRecord record) 
	{
		boolean copy = record.hasCopyCommand();
		String exp = record.getHighlightedText();
		return copy && ASTUtil.isExpression(exp);
	}

	
}

