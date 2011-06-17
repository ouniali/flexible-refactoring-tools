package JavaRefactoringAPI;

import java.util.ArrayList;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.internal.corext.refactoring.code.*;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;

import ASTree.CompilationUnitManipulationMethod;
import ExtractMethod.ASTExtractMethodChangeInformation;

public class JavaExtractMethodRefactoring extends JavaRefactoring{

	@SuppressWarnings("restriction")
	ExtractMethodRefactoring refactoring;
	ICompilationUnit unit;
	ASTExtractMethodChangeInformation information;
	static int extractedMethodCount =-1;
	
	public JavaExtractMethodRefactoring(ASTExtractMethodChangeInformation info)
	{
		information = info;
		extractedMethodCount ++;
	}
	@Override
	public void setEnvironment(ICompilationUnit u) {
		// TODO Auto-generated method stub
		unit = u;
	
	}

	@SuppressWarnings("restriction")
	@Override
	public void performRefactoring()  {
		// TODO Auto-generated method stub
		NullProgressMonitor monitor = new NullProgressMonitor();
		RefactoringStatus iniStatus;
		RefactoringStatus finStatus;
		int[] index;
	
		information.recoverICompilationUnitToOldRecord(unit);
	
		index = information.getSelectionStartAndEnd(unit);
		System.out.println(information.getCuttedSourceCode(unit));
		
		try{
			String source = unit.getSource();
			int selectionStart = index[0];
			int selectionLength =  index[1]-index[0]+1;
			System.out.println(source.substring(index[0],index[1]+1));
			refactoring = new ExtractMethodRefactoring(unit, selectionStart, selectionLength);
			refactoring.setMethodName(getExtractedMethodName());
			refactoring.setReplaceDuplicates(true);
			refactoring.setVisibility(Modifier.PRIVATE);
			iniStatus = refactoring.checkInitialConditions(monitor);
			System.out.println(selectionStart + " " + selectionLength);
			System.out.println(iniStatus.toString());
			if(!iniStatus.isOK())
				return;
			finStatus = refactoring.checkFinalConditions(monitor);
			System.out.println(finStatus.toString());
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
	
	String getExtractedMethodName()
	{
		if(extractedMethodCount == 0)
			return "extractedMethod";
		else
			return "extractedMethod" + extractedMethodCount;
	}

}