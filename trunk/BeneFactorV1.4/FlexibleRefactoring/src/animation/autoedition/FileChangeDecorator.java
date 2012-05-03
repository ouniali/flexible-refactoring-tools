package animation.autoedition;

import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class FileChangeDecorator implements ILightweightLabelDecorator{

	@Override
	public void addListener(ILabelProviderListener arg0) {
	
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		
	}

	@Override
	public void decorate(Object arg0, IDecoration arg1) {
		System.out.println(arg0.getClass());
		arg1.setBackgroundColor(new Color(Display.getCurrent(), 0, 0, 0));
	}

}
