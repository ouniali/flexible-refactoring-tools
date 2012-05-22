package ExtractMethod;

import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import ASTree.CompilationUnitHistoryRecord;

import compare.SourceDiff;
import compare.SourceDiffChange;
import compare.SourceDiffInsert;

public class NewMethodSignatureDetector {

	private static final int MAXIMUM_LOOK_BACK_COUNT_NEW_SIGNATURE = 5;
	private NewMethodSignature detected = null;
	
	public boolean isNewSignatureDetected(List<CompilationUnitHistoryRecord> records) 
	{	
		int total = getBackCount(records);
		for(int i = records.size() - 1; i > records.size() - 1 - total; i--)
		{
			CompilationUnitHistoryRecord current = records.get(i);
			String s = MethodSignaturehelper(current);
			if(!s.equals(""))
			{
				detected = new NewMethodSignature (s, current);
				return true;
			}
		}
		return false;
	}
	
	private int getBackCount(List<CompilationUnitHistoryRecord> records)
	{
		return Math.min(MAXIMUM_LOOK_BACK_COUNT_NEW_SIGNATURE, records.size());
	}
	
	
	public NewMethodSignature getNewSignature()
	{
		return detected;
	}
	
	private String MethodSignaturehelper(CompilationUnitHistoryRecord record)
	{
		SourceDiff diff = record.getSourceDiff();
		if(diff == null)
			return "";
		CompilationUnit tree = record.getASTree();
		ASTMethodDeclarationVisitor mVisitor = new ASTMethodDeclarationVisitor();
		tree.accept(mVisitor);
		int line = diff.getLineNumber();
		if(mVisitor.isInMethod(line))
			return "";
		else
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
	
}
