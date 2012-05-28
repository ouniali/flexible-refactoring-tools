package extract.declaration;

import java.util.StringTokenizer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;

import ASTree.CUHistory.CompilationUnitHistoryRecord;
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
		System.out.println(s);
		String[] tokens = StringUtil.removeEmptyTokens(StringUtil.splitBySpace(s));
		System.out.println(tokens.length);
		if( tokens.length > 1 && isType(tokens[0]) &&
			StringUtil.isJavaIdentifier(tokens[1]))
		{
			isNameAvailable = true;
			name = tokens[1];
			System.out.println(name);
		}
	}

	@Override
	public void setRefactoring(JavaRefactoring ref) 
	{
		JavaRefactoringELVBase elvb = (JavaRefactoringELVBase)ref;
		if(isNameAvailable)
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
