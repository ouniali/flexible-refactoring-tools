package UserAction;

import org.eclipse.mylyn.internal.monitor.ui.MonitorUiPlugin;

@SuppressWarnings("restriction")
public class MylynMonitor {
	
	static public void addListener()
	{
		MonitorUiPlugin monitor = MonitorUiPlugin.getDefault();
		monitor.addInteractionListener();
	}

}
