package JavaRefactoringAPI.extract.localvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;

import extract.localvariable.ELVCopy;
import extract.localvariable.ELVCut;

public class JavaRefactoringELVCopy extends JavaRefactoringELVBase{

	public JavaRefactoringELVCopy(ICompilationUnit u, int l, IMarker m, ELVCopy c)
			throws Exception {
		super(u, l, m, c);
	}

	@Override
	public JavaRefactoringELVBase moveRefactoring(IMarker marker, int l) throws Exception 
	{
		JavaRefactoringELVCopy elvc = new JavaRefactoringELVCopy(getICompilationUnit(), 
				l, marker, (ELVCopy)getActivity());
		elvc.setName(getTempName());
		elvc.setNonRefactoringChangeEnd(getNonRefactoringChangeEnd());
		return elvc;
	}	
	

}
