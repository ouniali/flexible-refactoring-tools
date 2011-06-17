package userinterface;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;

public class RenameMarkerResolution implements IMarkerResolution  {

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return "Finish Rename Automatically";
	}

	@Override
	public void run(IMarker marker) {
		// TODO Auto-generated method stub
	}

}
