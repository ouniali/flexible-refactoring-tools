package UserAction;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.mylyn.context.core.ContextCore;

import org.eclipse.mylyn.internal.monitor.ui.MonitorUiPlugin;
import org.eclipse.mylyn.internal.monitor.core.collection.CommandUsageCollector;
import org.eclipse.mylyn.monitor.core.IInteractionEventListener;
import org.eclipse.mylyn.monitor.core.InteractionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.BundleContext;


@SuppressWarnings({ "restriction", "unused" })

public class MylynMonitor {
	
	static public void addListener()
	{
		String rand = Math.random()+"";
		helper(rand);
		MonitorUiPlugin.getDefault().addInteractionListener(new BeneEventListener());
	}

	private static void helper(String rand) {
		ContextCore.getContextManager().activateContext(rand);
		ContextCore.getContextManager().deactivateContext(rand);
		ContextCore.getContextManager().deleteContext(rand);
	}
	

}



















