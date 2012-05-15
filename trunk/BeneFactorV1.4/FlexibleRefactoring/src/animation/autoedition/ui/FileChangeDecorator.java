package animation.autoedition.ui;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import flexiblerefactoring.Activator;



public class FileChangeDecorator implements ILightweightLabelDecorator{

	static ArrayList<ICompilationUnit> changedUnits = new ArrayList<ICompilationUnit>(); 	
	static ArrayList<IJavaElement> changedParents = new ArrayList<IJavaElement>();
	ArrayList<ILabelProviderListener> listeners = new ArrayList<ILabelProviderListener>();
	
	public static void addModifiedUnit(ICompilationUnit u)
	{
		changedUnits.add(u);		
		for(IJavaElement par = u.getParent(); !(par instanceof IJavaProject); par = par.getParent())
			changedParents.add(par);
	}
	
	public static void clearModifiedUnit()
	{
		changedUnits.clear();
		changedParents.clear();
	}
	
	@Override
	public void addListener(ILabelProviderListener lis) {
		listeners.add(lis);
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener lis) {
		listeners.remove(lis);
	}

	@Override
	public void decorate(Object ob, IDecoration dec) {
		ImageDescriptor des = Activator.getImageDescriptor(Activator.DECORATOR_ID);
		if(changedUnits.contains(ob) || changedParents.contains(ob))
			dec.addOverlay(des, IDecoration.TOP_RIGHT);
	}
	//TODO: not finished yet
	private void refresh()
	{
		for(ILabelProviderListener lis : listeners)
			lis.labelProviderChanged(null);
	}

}
