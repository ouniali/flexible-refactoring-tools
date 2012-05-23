package JavaRefactoringAPI.extractmethod;

import java.lang.reflect.Modifier;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

import ASTree.CompilationUnitHistoryRecord;
import ASTree.CompilationUnitManipulationMethod;
import ExtractMethod.ASTEMActivity;
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
	protected CompilationUnitHistoryRecord getRecordAfterRefactoring() {
		return activity.getRecord().getAllHistory().getMostRecentRecord();
	}

	@Override
	protected CompilationUnitHistoryRecord getRecordAfterRecovery() {
		return activity.getRecord();
	}

	@Override
	protected CompilationUnitHistoryRecord getNonrefactoringChangeStart() {
		return activity.getRecord();
	}



}