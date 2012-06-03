package extract.declaration;

import org.eclipse.jdt.core.ICompilationUnit;

import compare.SourceDiff;
import compare.SourceDiffIdentical;

import ASTree.CUHistory.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;

public abstract class Declaration {
	public abstract void setRefactoring(JavaRefactoring ref);
	public abstract void moveRefactoring(JavaRefactoring ref) throws Exception;
	
	protected CompilationUnitHistoryRecord getRecordNotEditingOn
		(CompilationUnitHistoryRecord record, int line)
	{
		CompilationUnitHistoryRecord current;
		CompilationUnitHistoryRecord after = null;
		
		for(current = record; current != null;current = current.getPreviousRecord())
		{
			after = current;
			SourceDiff diff = current.getSourceDiff();
			if(!current.hasMeaningfulChangedLineNumber())
				continue;
			if(diff.getLineNumber() != line)
				break;
		}
		
		if(current == null)
			return after;
		else
			return current;		
	}
	
	protected boolean isType(String token)
	{
		return (
				token.equals("boolean")
				|| token.equals("byte")
				|| token.equals("char")
				|| token.equals("double")
				|| token.equals("float")
				|| token.equals("int")
				|| token.equals("long")
				|| token.equals("short")
				|| token.equals("void"));
	}

}
