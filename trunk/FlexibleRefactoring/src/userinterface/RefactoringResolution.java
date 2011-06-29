package userinterface;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;

public abstract class RefactoringResolution implements IMarkerResolution{

	@Override
	public abstract String getLabel();

	@Override
	public abstract void run(IMarker marker);
}
