package animation.autoedition;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import abbot.Platform;

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
	public void decorate(Object ob, IDecoration dec) {
		if(ob instanceof ICompilationUnit)
		{
			
			dec.addSuffix(":affected");
		}
	}

}
