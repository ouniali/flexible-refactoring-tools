package UserAction;

import org.eclipse.core.internal.runtime.InternalPlatform;
import org.eclipse.mylyn.internal.monitor.ui.MonitorUiPlugin;
import org.eclipse.mylyn.monitor.ui.AbstractUserActivityMonitor;
import org.osgi.framework.BundleContext;

public class MylynUserMonitor extends AbstractUserActivityMonitor {

	@SuppressWarnings("restriction")
	public MylynUserMonitor() {
		// TODO Auto-generated constructor stub
		System.out.println("construction is called");
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		System.out.println("start is called");
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
		System.out.println("stop is called");
	}

}
