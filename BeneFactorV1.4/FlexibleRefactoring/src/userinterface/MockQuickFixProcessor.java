package userinterface;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;

import ASTree.ASTreeManipulationMethods;

public class MockQuickFixProcessor implements IQuickFixProcessor{

	//int targetLine = 10;
	IMarker marker; 
	boolean enable = false;
	@Override
	public boolean hasCorrections(ICompilationUnit unit, int problemId) {
		// TODO Auto-generated method stub
		return enable;
	}

	@Override
	public IJavaCompletionProposal[] getCorrections(IInvocationContext context,
			IProblemLocation[] locations) throws CoreException {
		// TODO Auto-generated method stub
		if(enable){
			ICompilationUnit unit = context.getCompilationUnit();
			CompilationUnit tree = ASTreeManipulationMethods.parseICompilationUnit(unit);
			int selection = context.getSelectionOffset();
			int line = tree.getLineNumber(selection);
			if(marker !=null && marker.exists())
				marker.delete();
			marker = RefactoringMarker.addRefactoringMarkerIfNo(unit, line);
			return new IJavaCompletionProposal[]{new MockProposal()};
		}
		else 
			return null;
	}

}
