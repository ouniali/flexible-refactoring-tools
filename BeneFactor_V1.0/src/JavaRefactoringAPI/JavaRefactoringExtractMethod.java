package JavaRefactoringAPI;

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
	static int extractedMethodCount = -1;
	long WAIT_TIME = 2000;

	public JavaRefactoringExtractMethod(ICompilationUnit u, int l,IMarker m,ASTExtractMethodChangeInformation info) {
		super(u, l, m);
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
		if (extractedMethodCount == 0)
			return "extractedMethod";
		else
			return "extractedMethod" + extractedMethodCount;
	}

	@Override
	protected void performCodeRecovery() {
		information.recoverICompilationUnitToOldRecord(this.getICompilationUnit());
		
	}


	@Override
	public int getRefactoringType() {
		return JavaRefactoringType.EXTRACT_METHOD;
	}



}
