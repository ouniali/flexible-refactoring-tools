package JavaRefactoringAPI.extract.localvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

import extract.localvariable.ELVCut;

final public class JavaRefactoringELVCut extends JavaRefactoringELVBase{
	
	public JavaRefactoringELVCut(ICompilationUnit u, int l, ELVCut c)
			throws Exception {
		super(u, l, c);
	}

	

}
