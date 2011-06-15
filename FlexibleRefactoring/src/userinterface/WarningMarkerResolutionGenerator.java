package userinterface;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.internal.ui.text.correction.*;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class WarningMarkerResolutionGenerator implements
		IMarkerResolutionGenerator {

	@Override
	public IMarkerResolution[] getResolutions(IMarker marker) {
		System.out.println("ddddddddddddddd");
		return new IMarkerResolution[] {
				new RefactoringProblemMarkerResolution(),
				new RefactoringProblemMarkerResolution(),
		};
	}

}
