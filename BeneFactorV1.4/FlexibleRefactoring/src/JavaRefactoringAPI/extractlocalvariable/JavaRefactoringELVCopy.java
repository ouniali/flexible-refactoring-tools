package JavaRefactoringAPI.extractlocalvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;

public class JavaRefactoringELVCopy extends JavaRefactoringELVBase{

	public JavaRefactoringELVCopy(ICompilationUnit u, int l, IMarker m)
			throws Exception {
		super(u, l, m);
	}

}