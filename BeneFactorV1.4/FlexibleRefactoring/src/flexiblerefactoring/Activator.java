package flexiblerefactoring;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import util.FileUtil;

import ASTree.ASTChange;
import ASTree.CUHistory.CompilationUnitHistoryRecord;
import UserAction.MylynMonitor;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "BeneFactor"; //$NON-NLS-1$
	public static final String ICON_ID = "BeneFactorIcon";
	public static final String DECORATOR_ID = "BeneFactorDecorator";
	
	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		MylynMonitor.addListener();
		// add mylyn monitor
		FileUtil.deleteFolder(CompilationUnitHistoryRecord.getHistoryFilesRoot());
		FileUtil.deleteFolder(ASTChange.getChangeFilesRoot());
		// delete files for the last time
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	
	protected void initializeImageRegistry(ImageRegistry registry) {
        super.initializeImageRegistry(registry);
        Bundle bundle = Platform.getBundle(PLUGIN_ID);

        ImageDescriptor myImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/refactoring.png"), null));
        registry.put(ICON_ID, myImage);
        myImage = ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/decorator.png"), null));
        registry.put(DECORATOR_ID, myImage);
        
    }
	
	
	
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String image_id) {
		AbstractUIPlugin plugin = Activator.getDefault();
		ImageRegistry imageRegistry = plugin.getImageRegistry();
		Image ima = imageRegistry.get(image_id);
		return ImageDescriptor.createFromImage(ima);
	
	}
}
