package JavaRefactoringAPI;

import java.util.ArrayList;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.internal.corext.refactoring.code.*;
import org.eclipse.ltk.core.refactoring.Change;

public class JavaExtractMethodRefactoring extends JavaRefactoring{

	@SuppressWarnings("restriction")
	ExtractMethodRefactoring refactoring;
	ICompilationUnit unit;
	int SelectionStart;
	int SelectionLength;
	static String NewMethodName = "ExtractedMethod";
	
	public JavaExtractMethodRefactoring(int start, int length)
	{
		SelectionStart = start;
		SelectionLength = length;
	}
	@SuppressWarnings("restriction")
	@Override
	public void setEnvironment(ICompilationUnit u) {
		// TODO Auto-generated method stub
		unit = u;
		refactoring = new ExtractMethodRefactoring(unit, SelectionStart, SelectionLength);
		refactoring.setMethodName(NewMethodName);
		refactoring.setReplaceDuplicates(false);
		refactoring.setVisibility(Modifier.PRIVATE);
	}

	@SuppressWarnings("restriction")
	@Override
	public void performRefactoring() {
		// TODO Auto-generated method stub
		NullProgressMonitor monitor = new NullProgressMonitor();
		try{
			refactoring.checkInitialConditions(monitor);
			refactoring.checkFinalConditions(monitor);
			Change change = refactoring.createChange(monitor);
			change.perform(monitor);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkPreconditions() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkPostconditions() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void run()
	{
		performRefactoring();
	}

}
