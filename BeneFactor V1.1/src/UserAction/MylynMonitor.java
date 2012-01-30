package UserAction;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.mylyn.internal.monitor.ui.MonitorUiPlugin;
import org.eclipse.mylyn.internal.monitor.core.collection.CommandUsageCollector;
import org.osgi.framework.BundleContext;

@SuppressWarnings({ "restriction", "unused" })

public class MylynMonitor {
	
	static public void addListener()
	{
		MonitorUiPlugin monitor = MonitorUiPlugin.getDefault();
		BundleContext context = InternalPlatform.getDefault().getBundleContext();
		monitor.addInteractionListener(new CopyEventListener());
		monitor.addInteractionListener(new CutEventListener());
		try {
		
			monitor.setDebugging(true);
			monitor.start(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
