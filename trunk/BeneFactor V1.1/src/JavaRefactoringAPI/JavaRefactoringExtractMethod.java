package JavaRefactoringAPI;

import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.internal.corext.refactoring.code.*;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import ExtractMethod.ASTExtractMethodChangeInformation;

public class JavaRefactoringExtractMethod extends JavaRefactoring {

	@SuppressWarnings("restriction")
	ExtractMethodRefactoring refactoring;
	ASTExtractMethodChangeInformation information;
	
	static int extractedMethodCount = 0;
	int modifier;
	String returnType;
	String methodName;
	
	
	
	public JavaRefactoringExtractMethod(ICompilationUnit u, int l,IMarker m,ASTExtractMethodChangeInformation info) {
		super(u, l, m);
		modifier = Modifier.PRIVATE;
		methodName = getExtractedMethodName();
		information = info;
		
	}

	
	@SuppressWarnings("restriction")
	@Override
	public void performRefactoring() {
		// TODO Auto-generated method stub
		NullProgressMonitor monitor = new NullProgressMonitor();
		RefactoringStatus iniStatus;
		RefactoringStatus finStatus;
		int[] index;

		index = information.getSelectionStartAndEnd(this.getICompilationUnit());

		try {
			int selectionStart = index[0];
			int selectionLength = index[1] - index[0] + 1;
			refactoring = new ExtractMethodRefactoring(this.getICompilationUnit(), selectionStart,
					selectionLength);
			refactoring.setReplaceDuplicates(true);
			refactoring.setVisibility(modifier);
			refactoring.setMethodName(methodName);
			// wait for the underlying resource to be ready
			Thread.sleep(WAIT_TIME);
			iniStatus = refactoring.checkInitialConditions(monitor);
			if (!iniStatus.isOK())
				return;
			finStatus = refactoring.checkFinalConditions(monitor);
			if (!finStatus.isOK())
				return;
			Change change = refactoring.createChange(monitor);
			Change undo = change.perform(monitor);
			this.setUndo(undo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	String getExtractedMethodName() {
		int index = extractedMethodCount;
		extractedMethodCount ++;
		if (index == 0)
			return "extractedMethod";
		else
			return "extractedMethod" + Integer.toString(index);
	}

	@Override
	protected void performCodeRecovery() {
		information.recoverICompilationUnitToOldRecord();
		
	}


	@Override
	public int getRefactoringType() {
		return JavaRefactoringType.EXTRACT_METHOD;
	}
	
	public ASTExtractMethodChangeInformation getExtractMethodChangeInformation()
	{
		return information;
	}
	
	public void setMethodModifier(int m)
	{
		modifier = m;
	}
	
	public void setMethodName(String m)
	{
		methodName = m;
	}
	
	public JavaRefactoringExtractMethod moveExtractMethodRefactoring(IMarker marker, int l)
	{
		JavaRefactoringExtractMethod refactoring = new JavaRefactoringExtractMethod(getICompilationUnit(), l, marker, getExtractMethodChangeInformation());
		refactoring.setMethodModifier(modifier);
		refactoring.setMethodName(methodName);
		return refactoring; 
	}

}
