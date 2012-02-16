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
	
	static public void addListener1()
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
	
	static private void addListener()
	{
		String rand = Math.random()+"";
		ContextCore.getContextManager().activateContext(rand);
		ContextCore.getContextManager().deactivateContext(rand);
		ContextCore.getContextManager().deleteContext(rand);		
		
		
		MonitorUiPlugin.getDefault().addInteractionListener(new IInteractionEventListener() {	
			@Override
			public void interactionObserved(InteractionEvent event) {	
			}
		
		
			@Override
			public void startMonitoring() {}
		
			@Override
			public void stopMonitoring() {}
		});
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		Display display = workbench.getDisplay();
		
		Listener listener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {							
			}
		};
		
		display.addFilter(SWT.KeyUp, listener);
		display.addFilter(SWT.MouseUp, listener);
		display.addFilter(SWT.Arm, listener);
	}

}
