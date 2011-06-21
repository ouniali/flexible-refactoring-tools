package userinterface;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

import JavaRefactoringAPI.JavaRefactoringType;

public class RefactoringProblemMarkerResolutionGenerator implements
		IMarkerResolutionGenerator {

	@Override
	public IMarkerResolution[] getResolutions(IMarker marker) {
		// TODO Auto-generated method stub
		
		int type = marker.getAttribute("REFACTORING_TYPE", JavaRefactoringType.UNCERTAIN);	
		return new IMarkerResolution[] {
				getRefactoringResolution(type)
		};
	}
	
	static public IMarkerResolution getRefactoringResolution(int type)
	{
		switch(type)
		{
		case JavaRefactoringType.RENAME:
			return new RenameMarkerResolution();
		case JavaRefactoringType.EXTRACT_METHOD:
			return new ExtractMethodMarkerResolution();
		default:
			return new UncertainMethodMarkerResolution();
		}
	}

}
