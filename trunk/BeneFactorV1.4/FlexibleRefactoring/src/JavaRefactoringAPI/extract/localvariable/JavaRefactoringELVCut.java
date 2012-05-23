package JavaRefactoringAPI.extract.localvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

import extract.localvariable.ELVCut;

final public class JavaRefactoringELVCut extends JavaRefactoringELVBase{
	
	public JavaRefactoringELVCut(ICompilationUnit u, int l, IMarker m, ELVCut c)
			throws Exception {
		super(u, l, m, c);
	}

	@Override
	public JavaRefactoringELVBase moveRefactoring(IMarker marker, int l) throws Exception {
		JavaRefactoringELVCut elvc = new JavaRefactoringELVCut(getICompilationUnit(), 
				l, marker, (ELVCut)getActivity());
		elvc.setName(getTempName());
		elvc.setNonRefactoringChangeEnd(getNonRefactoringChangeEnd());
		return elvc;
	}
	
	

}
