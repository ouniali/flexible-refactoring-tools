package UserAction;

import org.eclipse.mylyn.monitor.core.IInteractionEventListener;
import org.eclipse.mylyn.monitor.core.InteractionEvent;
import org.eclipse.mylyn.monitor.core.InteractionEvent.Kind;
import org.eclipse.mylyn.monitor.core.IInteractionEventListener;

import utitilies.UserInterfaceUtilities;

public class CopyEventListener implements IInteractionEventListener {


	@Override
	public void interactionObserved(InteractionEvent event) {
		if(!event.getOriginId().equals("org.eclipse.ui.edit.copy"))
			return;
		System.out.println("copy");
		
		int[] range = UserInterfaceUtilities.getSelectedRangeInActiveEditor();
		System.out.println(range[0] + " " + range[1]);
	}

	@Override
	public void startMonitoring() {
	
	}

	@Override
	public void stopMonitoring() {
	}



}
