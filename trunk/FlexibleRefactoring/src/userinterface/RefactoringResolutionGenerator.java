package userinterface;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

import JavaRefactoringAPI.JavaRefactoringType;

public class RefactoringResolutionGenerator implements
		IMarkerResolutionGenerator {

	@Override
	public IMarkerResolution[] getResolutions(IMarker marker) {
		// TODO Auto-generated method stub
		int type = marker.getAttribute("REFACTORING_TYPE", JavaRefactoringType.UNCERTAIN_LOW);
		
		return new IMarkerResolution[]{
				getRefactoringResolution(type)
		};
	}
	private RefactoringResolution getRefactoringResolution(int type)
	{
		switch(type)
		{
		case JavaRefactoringType.RENAME:
			return new RefactoringResolutionRename();
		case JavaRefactoringType.EXTRACT_METHOD:
			return new RefactoringResolutionExtractMethod();
		default:
			return null;
		}
	}
}
