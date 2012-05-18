package extractlocalvariable;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;

import util.ASTUtil;

import ASTree.ASTChangeInformationGenerator;
import ASTree.CompilationUnitHistoryRecord;
import ASTree.NewRootPair;
import JavaRefactoringAPI.JavaRefactoring;

public class ExtractLocalVariableDetector {
	
	private final int LOOK_BACK_COUNT = 5;
	ExtractLocalVariableActivity act1;
	ExtractLocalVariableActivity act2;

	
	public boolean isELVFound(List<CompilationUnitHistoryRecord> records)
	{
		act1 = getELVActivityOrNull(records, new CutDetectStrategy());
		act2 = getELVActivityOrNull(records, new CopyDetectStrategy());
		return !(act1 == null && act2 == null);
	}
	
	public JavaRefactoring getELVRefactoring(ICompilationUnit unit) throws Exception
	{
		if(act1 != null && act2!= null)
			return DecideLaterActivity().getELVRefactoring(unit);
		else if(null != act1)
			return act1.getELVRefactoring(unit);
		else if(null != act2)
			return act2.getELVRefactoring(unit);
		else
			return null;

	}
	
	private ExtractLocalVariableActivity DecideLaterActivity()
	{
		if(act1.getRecord().getTime() < act2.getRecord().getTime())
			return act2;
		else 
			return act1;
	}
	
	private ExtractLocalVariableActivity getELVActivityOrNull(
			List<CompilationUnitHistoryRecord> records, DetectStrategy str)
	{
		int lookBack = Math.min(records.size() - 1, LOOK_BACK_COUNT);
		for(int i = 0; i <= lookBack; i ++)
		{
			int index = records.size() - 1 - i;
			CompilationUnitHistoryRecord current = records.get(index);
			if(str.isELVRecord(current))
				return str.getELVActivity(current);
		}
		return null;
	}
}

interface DetectStrategy
{
	public boolean isELVRecord(CompilationUnitHistoryRecord record);
	public ExtractLocalVariableActivity getELVActivity(CompilationUnitHistoryRecord r);
}

class CutDetectStrategy implements DetectStrategy
{
	public boolean isELVRecord(CompilationUnitHistoryRecord record) 
	{
		boolean cut = record.hasCutCommand();
		String exp = record.getHighlightedText();
		boolean res = cut && ASTUtil.isExpression(exp);
		return res;
	}

	public ExtractLocalVariableActivity getELVActivity(CompilationUnitHistoryRecord r) {
		ExtractLocalVariableCut cut1 = ExtractLocalVariableCut.getCurrentInstance();
		ExtractLocalVariableCut cut2 = ExtractLocalVariableCut.getNewInstance(r);
		if(null != cut1 && !cut1.getRecord().equals(cut2.getRecord()))
			return cut2;
		else return null;
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

	@Override
	public ExtractLocalVariableActivity getELVActivity(CompilationUnitHistoryRecord r) {
		ExtractLocalVariableCopy cop1 = ExtractLocalVariableCopy.getCurrentInstance();
		ExtractLocalVariableCopy cop2 = ExtractLocalVariableCopy.getNewInstance(r);
		if(null != cop1 && !cop1.getRecord().equals(cop2.getRecord()))
			return cop2;
		else 
			return null;
	}

	
}

