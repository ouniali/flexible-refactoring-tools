package UserAction;

import org.eclipse.mylyn.monitor.core.IInteractionEventListener;
import org.eclipse.mylyn.monitor.core.InteractionEvent;
import org.eclipse.mylyn.monitor.core.InteractionEvent.Kind;
import org.eclipse.mylyn.monitor.core.IInteractionEventListener;

import utitilies.UserInterfaceUtilities;

public class BeneEventListener implements IInteractionEventListener {


	@Override
	public void interactionObserved(InteractionEvent event) {
		String id = event.getOriginId();
		if(UserActionData.isInterestedEvent(id))
			UserActionData.setPendingEvent(id);
	}

	@Override
	public void startMonitoring() {
	
	}

	@Override
	public void stopMonitoring() {
	}



}
