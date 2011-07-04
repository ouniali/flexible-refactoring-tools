package JavaRefactoringAPI;

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
	static int extractedMethodCount = -1;
	long WAIT_TIME = 2000;

	public JavaRefactoringExtractMethod(ICompilationUnit u, ASTExtractMethodChangeInformation info) {
		super(u);
		information = info;
		extractedMethodCount++;
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
			refactoring.setMethodName(getExtractedMethodName());
			refactoring.setReplaceDuplicates(true);
			refactoring.setVisibility(Modifier.PRIVATE);
			// wait for the underlying resource to be ready
			
			iniStatus = refactoring.checkInitialConditions(monitor);
			if (!iniStatus.isOK())
				return;
			finStatus = refactoring.checkFinalConditions(monitor);
			if (!finStatus.isOK())
				return;
			Change change = refactoring.createChange(monitor);
			change.perform(monitor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	String getExtractedMethodName() {
		if (extractedMethodCount == 0)
			return "extractedMethod";
		else
			return "extractedMethod" + extractedMethodCount;
	}

	@Override
	protected void performCodeRecovery() {
		// TODO Auto-generated method stub
		information.recoverICompilationUnitToOldRecord(this.getICompilationUnit());
		
	}


}
