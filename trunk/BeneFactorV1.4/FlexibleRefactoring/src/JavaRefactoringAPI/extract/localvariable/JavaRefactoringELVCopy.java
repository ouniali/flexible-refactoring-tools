package JavaRefactoringAPI.extract.localvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;

import extract.localvariable.ELVCopy;
import extract.localvariable.ELVCut;

public class JavaRefactoringELVCopy extends JavaRefactoringELVBase{

	public JavaRefactoringELVCopy(ICompilationUnit u, int l, ELVCopy c)
			throws Exception {
		super(u, l, c);
	}


	

}
