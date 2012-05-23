package JavaRefactoringAPI.extractlocalvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

import extractlocalvariable.ELVCut;

final public class JavaRefactoringELVCut extends JavaRefactoringELVBase{
	
	public JavaRefactoringELVCut(ICompilationUnit u, int l, IMarker m, ELVCut c)
			throws Exception {
		super(u, l, m, c);
	}
	
	

}
