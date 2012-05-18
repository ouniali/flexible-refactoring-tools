package JavaRefactoringAPI.extractlocalvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

import extractlocalvariable.ExtractLocalVariableCut;

final public class JavaRefactoringELVCut extends JavaRefactoringELVBase{

	ExtractLocalVariableCut cut;
	
	public JavaRefactoringELVCut(ICompilationUnit u, int l, IMarker m, ExtractLocalVariableCut c)
			throws Exception {
		super(u, l, m);
		cut = c;
	}
	
	protected void performCodeRecovery(IProgressMonitor pm) throws Exception {
		
	}

}
