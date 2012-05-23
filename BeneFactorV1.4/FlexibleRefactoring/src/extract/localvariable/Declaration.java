package extract.localvariable;

import org.eclipse.jdt.core.ICompilationUnit;

import compare.SourceDiff;
import compare.SourceDiffNull;

import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;

public abstract class Declaration {
	public abstract void setRefactoring(JavaRefactoring ref);
	public abstract JavaRefactoring moveRefactoring(JavaRefactoring ref, ICompilationUnit unit) throws Exception;
	
	protected CompilationUnitHistoryRecord getRecordNonRefactoringChangeEnd
		(CompilationUnitHistoryRecord record, int line)
	{
		CompilationUnitHistoryRecord current;
		CompilationUnitHistoryRecord after = null;
		
		for(current = record; current != null;current = current.getPreviousRecord())
		{
			after = current;
			SourceDiff diff = current.getSourceDiff();
			if(diff instanceof SourceDiffNull)
				continue;
			if(diff.getLineNumber() != line)
				break;
		}
		
		if(current == null)
			return after;
		else
			return current;		
	}
}
