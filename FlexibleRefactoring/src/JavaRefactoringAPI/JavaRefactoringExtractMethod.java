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
	ICompilationUnit unit;
	ASTExtractMethodChangeInformation information;
	static int extractedMethodCount = -1;
	long WAIT_TIME = 2000;

	public JavaRefactoringExtractMethod(ASTExtractMethodChangeInformation info) {
		information = info;
		extractedMethodCount++;
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

		information.recoverICompilationUnitToOldRecord(unit);
		index = information.getSelectionStartAndEnd(unit);

		try {
			int selectionStart = index[0];
			int selectionLength = index[1] - index[0] + 1;
			refactoring = new ExtractMethodRefactoring(unit, selectionStart,
					selectionLength);
			refactoring.setMethodName(getExtractedMethodName());
			refactoring.setReplaceDuplicates(true);
			refactoring.setVisibility(Modifier.PRIVATE);
			// always output true for the statement
			/*
			 * System.out.println(unit.isConsistent() &&
			 * !unit.getBuffer().hasUnsavedChanges() &&
			 * !unit.hasResourceChanged() && !unit.hasUnsavedChanges() &&
			 * unit.isStructureKnown() && unit.isOpen() && !unit.isReadOnly() &&
			 * unit.isWorkingCopy() && !unit.getBuffer().isClosed() &&
			 * !unit.getBuffer().isReadOnly() &&
			 * unit.getResource().isAccessible() &&
			 * unit.getResource().isSynchronized(1));
			 *///
				// wait for the underlying resource to be ready
			Thread.sleep(WAIT_TIME);
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

	public void run() {
		performRefactoring();
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
		
	}


}
