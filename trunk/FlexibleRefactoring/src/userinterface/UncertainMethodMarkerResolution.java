package userinterface;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;

public class UncertainMethodMarkerResolution implements IMarkerResolution {

	@Override
	public String getLabel() {
		return "Uncertain Refactoring Type";
	}

	@Override
	public void run(IMarker marker) {
		// TODO Auto-generated method stub

	}

}
