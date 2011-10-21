package JavaRefactoringAPI;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.internal.corext.refactoring.code.ExtractMethodRefactoring;
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
	public void performRefactoring(IProgressMonitor pm) {
		// TODO Auto-generated method stub

		SubMonitor monitor = SubMonitor.convert(pm,"Performing Extract Method Refactoring",4);
		
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
			iniStatus = refactoring.checkInitialConditions(monitor.newChild(1));
			if (!iniStatus.isOK())
				return;
			finStatus = refactoring.checkFinalConditions(monitor.newChild(1));
			if (!finStatus.isOK())
				return;
			Change change = refactoring.createChange(monitor.newChild(1));
			Change undo = change.perform(monitor.newChild(1));
			this.setUndo(undo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		monitor.done();
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
	protected void performCodeRecovery(IProgressMonitor monitor) {
		information.recoverICompilationUnitToOldRecord(monitor);
		
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
