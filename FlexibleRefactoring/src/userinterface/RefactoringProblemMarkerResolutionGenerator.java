package userinterface;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class RefactoringProblemMarkerResolutionGenerator implements
		IMarkerResolutionGenerator {

	@Override
	public IMarkerResolution[] getResolutions(IMarker marker) {
		// TODO Auto-generated method stub
		 
		return new IMarkerResolution[] {
				new RefactoringProblemMarkerResolution(),
				new RefactoringProblemMarkerResolution(),
		};
	}

}
