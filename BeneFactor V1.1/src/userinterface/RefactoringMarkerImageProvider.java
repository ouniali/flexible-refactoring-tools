package userinterface;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.internal.ide.IMarkerImageProvider;

import flexiblerefactoring.BeneFactor;

public class RefactoringMarkerImageProvider implements IMarkerImageProvider{


	public String getImagePath(IMarker marker) {
		// TODO Auto-generated method stub
		return BeneFactor.getIconPath("refactoring.png");
	}

}
