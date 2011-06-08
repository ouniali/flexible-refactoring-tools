package JavaRefactoringAPI;

import java.util.ArrayList;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.internal.corext.refactoring.code.*;
import org.eclipse.ltk.core.refactoring.Change;

import ExtractMethod.ASTExtractMethodChangeInformation;

public class JavaExtractMethodRefactoring extends JavaRefactoring{

	@SuppressWarnings("restriction")
	ExtractMethodRefactoring refactoring;
	ICompilationUnit unit;
	ASTExtractMethodChangeInformation information;
	String newMethodName;
	
	public JavaExtractMethodRefactoring(ASTExtractMethodChangeInformation info)
	{
		information = info;
		newMethodName ="ExtractedMethod";
	}
	@Override
	public void setEnvironment(ICompilationUnit u) {
		// TODO Auto-generated method stub
		unit = u;
	
	}

	@SuppressWarnings("restriction")
	@Override
	public void performRefactoring() {
		// TODO Auto-generated method stub
		NullProgressMonitor monitor = new NullProgressMonitor();
		int[] index;
		information.recoverICompilationUnitToBeforeCutting(unit);
		index = information.getSelectionStartAndEnd(unit);
		
		try{
			refactoring = new ExtractMethodRefactoring(unit, index[0], index[1]);
			refactoring.setMethodName(newMethodName);
			refactoring.setReplaceDuplicates(false);
			refactoring.setVisibility(Modifier.PRIVATE);
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
