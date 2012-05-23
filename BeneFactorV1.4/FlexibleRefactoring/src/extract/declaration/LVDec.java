package extract.declaration;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;

import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.extract.localvariable.JavaRefactoringELVBase;
import userinterface.RefactoringMarker;
import util.StringUtil;

public class LVDec extends Declaration{

	int line;
	String name;
	boolean isNameAvailable = false;
	CompilationUnitHistoryRecord record;
	
	public LVDec (int l, String t, CompilationUnitHistoryRecord r)
	{
		parse(t);
		record = r;
		line = l;
	}
	
	private void parse(String s)
	{
		String[] tokens = s.split(" ");
		if(tokens.length > 1 && StringUtil.isJavaIdentifier(tokens[1]))
		{
			isNameAvailable = true;
			name = tokens[1];
		}
		else
			isNameAvailable = false;
	}

	@Override
	public void setRefactoring(JavaRefactoring ref) 
	{
		JavaRefactoringELVBase elvb = (JavaRefactoringELVBase)ref;
		elvb.setTempName(name);
		elvb.setNonRefactoringChangeEnd(getRecordNotEditingOn(record, line));
	}

	@Override
	public void moveRefactoring(JavaRefactoring ref) throws Exception 
	{
		JavaRefactoringELVBase elvb = (JavaRefactoringELVBase)ref;
		ref.moveRefactoring(line);
	}
	
	
	
}
