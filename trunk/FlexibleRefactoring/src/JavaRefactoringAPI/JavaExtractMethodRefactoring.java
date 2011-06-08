package JavaRefactoringAPI;

import java.util.ArrayList;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.internal.corext.refactoring.code.*;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

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
		newMethodName ="extractedMethod";
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
		RefactoringStatus iniStatus;
		RefactoringStatus finStatus;
		int[] index;
		information.recoverICompilationUnitToBeforeCutting(unit);
		index = information.getSelectionStartAndEnd(unit);
		
		try{
			refactoring = new ExtractMethodRefactoring(unit, index[0], index[1]-index[0]+1);
			refactoring.setMethodName(newMethodName);
			refactoring.setReplaceDuplicates(false);
			refactoring.setVisibility(Modifier.PRIVATE);
			iniStatus = refactoring.checkInitialConditions(monitor);
			if(!iniStatus.isOK())
				return;
			finStatus = refactoring.checkFinalConditions(monitor);
			if(!finStatus.isOK())
				return;
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
