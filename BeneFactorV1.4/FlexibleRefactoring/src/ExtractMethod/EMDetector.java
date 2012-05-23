package ExtractMethod;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;

import util.ASTUtil;
import util.StringUtil;
import ASTree.ASTChangeInformationGenerator;
import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;

import compare.SourceDiff;
import compare.SourceDiffChange;
import compare.SourceDiffInsert;


public class EMDetector {
	
	public static final int MAXIMUM_LOOK_BACK_COUNT_EXTRACT_METHOD = 5;



	

	

	
	//looking back for extract method change, null if not found.
	private ASTEMChangeInformation 
		LookingBackForDetectingExtractMethodChange(ArrayList<CompilationUnitHistoryRecord> Records) 
	{
		if (Records.size() == 0)
			return null;
		CompilationUnitHistoryRecord RecordTwo = Records
				.get(Records.size() - 1);

		if (Records.size() <= 1)
			return null;

		int lookBackCount = Math.min(Records.size() - 1,
				MAXIMUM_LOOK_BACK_COUNT_EXTRACT_METHOD);
		CompilationUnitHistoryRecord RecordOne;

		for (int i = 1; i <= lookBackCount; i++) {
			int index = Records.size() - 1 - i;
			RecordOne = Records.get(index);
			ASTEMChangeInformation change = ASTChangeInformationGenerator
					.getExtractMethodASTChangeInformation(RecordOne, RecordTwo);
			if (change != null) {
				return change;
			}
		}
		return null;
	}
	
	//looking back for extract method activity, null if not found.
	private ASTEMActivity 
		LookingBackForExtractMethodActivities(ArrayList<CompilationUnitHistoryRecord> records)
	{	
		int lookBackCount = Math.min(records.size(),
				MAXIMUM_LOOK_BACK_COUNT_EXTRACT_METHOD);
	
		for(int i = records.size() - 1; i >= records.size() - lookBackCount; i--)
		{
			CompilationUnitHistoryRecord current = records.get(i);
			ASTEMActivity activity = new ASTEMActivity(current);
			if(ASTEMActivity.isCopyingStatements(current))
				return activity;
		}
		return null;
	}

	





	public boolean isExtractMethodDetected(ArrayList<CompilationUnitHistoryRecord> records) {
		ASTEMChangeInformation change = LookingBackForDetectingExtractMethodChange(records);
		ASTEMActivity act = LookingBackForExtractMethodActivities(records);
		return !(change == null && act == null);
	}

	public JavaRefactoring getEMRefactoring(
			ArrayList<CompilationUnitHistoryRecord> records, ICompilationUnit unit) throws Exception {
		ASTEMChangeInformation change = LookingBackForDetectingExtractMethodChange(records);
		ASTEMActivity act = LookingBackForExtractMethodActivities(records);
		if(change != null)
			return getCutRefactoring(change, unit);
		else
			return getCopyRefactoring(act, unit);
	}
	
	private JavaRefactoring getCutRefactoring
		(ASTEMChangeInformation change, ICompilationUnit unit) throws Exception
	{
		ExtractWithCut.getInstance().set(change);
		return ExtractWithCut.getInstance().getRefactoring(unit);
	}
	
	private static JavaRefactoring getCopyRefactoring
		(ASTEMActivity act, ICompilationUnit unit) throws Exception
	{
		ExtractWithCopy.getInstance().set(act);
		return ExtractWithCopy.getInstance().getRefactoring(unit);
	}

}
