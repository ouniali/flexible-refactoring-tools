package JavaRefactoringAPI.extract.method;

import java.lang.reflect.Modifier;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;

import extract.method.ASTEMActivity;

import ASTree.CompilationUnitHistoryRecord;
import ASTree.CompilationUnitManipulationMethod;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;

public class JavaRefactoringExtractMethodActivity extends JavaRefactoringExtractMethodBase{

	final ASTEMActivity activity;
		
	public JavaRefactoringExtractMethodActivity(ICompilationUnit u, int l, 
			IMarker m, ASTEMActivity a) throws Exception{
		super(u, l, m);
		activity = a;
	}

	@Override
	protected void performRefactoring(IProgressMonitor pm) throws Exception {
		JavaRefactoringExtractMethodUtil.performEclipseRefactoring(this.getICompilationUnit(), 
				activity.getCopyStart(), activity.getCopyLength(), 
				this.getModifier(), this.getMethodName(), pm);
	}

	@Override
	protected void performCodeRecovery(IProgressMonitor pm) throws Exception {
		CompilationUnitManipulationMethod.UpdateICompilationUnit(
				this.getICompilationUnit(), activity.getRecord().getSourceCode(), null);
	}


	@Override
	public JavaRefactoringExtractMethodBase moveExtractMethodRefactoring(
			IMarker marker, int l) throws Exception {
		JavaRefactoringExtractMethodActivity ref = new JavaRefactoringExtractMethodActivity
				(this.getICompilationUnit(), l, marker, activity);
		ref.setMethodName(getMethodName());
		ref.setModifier(getModifier());
		return ref;
	}



	@Override
	protected String getSourceAfterRefactoring() throws Exception {
		return getICompilationUnit().getSource();
	}

	@Override
	protected String getSourceAfterRecovery() throws Exception {
		return activity.getRecord().getSourceCode();
	}

	@Override
	protected CompilationUnitHistoryRecord getNonrefactoringChangeStart() {
		return activity.getRecord();
	}

	@Override
	protected CompilationUnitHistoryRecord getLatestRecord() throws Exception {
		return activity.getRecord().getAllHistory().getMostRecentRecord();
	}



}
