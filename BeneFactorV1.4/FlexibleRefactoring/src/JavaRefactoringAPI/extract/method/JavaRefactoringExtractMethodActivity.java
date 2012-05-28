package JavaRefactoringAPI.extract.method;

import java.lang.reflect.Modifier;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;

import util.ICompilationUnitUtil;

import extract.method.ASTEMActivity;

import ASTree.CUHistory.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;

public class JavaRefactoringExtractMethodActivity extends JavaRefactoringExtractMethodBase{

	final ASTEMActivity activity;
		
	public JavaRefactoringExtractMethodActivity(ICompilationUnit u, int l, ASTEMActivity a) throws Exception{
		super(u, l);
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
		ICompilationUnitUtil.UpdateICompilationUnit(
				this.getICompilationUnit(), activity.getRecord().getSourceCode(), null);
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
