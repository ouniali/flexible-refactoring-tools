package animation.autoedition;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flexiblerefactoring.Activator;

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
		
		ImageDescriptor des = Activator.getImageDescriptor(Activator.DECORATOR_ID);
		if(ob instanceof ICompilationUnit)
		{
			dec.addOverlay(des, IDecoration.TOP_RIGHT);
		}
	}

}
