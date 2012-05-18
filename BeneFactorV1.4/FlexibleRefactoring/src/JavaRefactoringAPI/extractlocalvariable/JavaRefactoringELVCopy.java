package JavaRefactoringAPI.extractlocalvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;

import extractlocalvariable.ExtractLocalVariableCopy;

public class JavaRefactoringELVCopy extends JavaRefactoringELVBase{

	public JavaRefactoringELVCopy(ICompilationUnit u, int l, IMarker m, ExtractLocalVariableCopy c)
			throws Exception {
		super(u, l, m, c);
	}
	

}
