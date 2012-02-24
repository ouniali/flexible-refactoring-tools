package UserAction;

import org.eclipse.mylyn.monitor.core.IInteractionEventListener;
import org.eclipse.mylyn.monitor.core.InteractionEvent;

public class BeneFactorEventListener implements IInteractionEventListener{

	@Override
	public void interactionObserved(InteractionEvent event) {
		String id = event.getOriginId();
		System.out.println(id);
		
	}

	@Override
	public void startMonitoring() {
		
	}

	@Override
	public void stopMonitoring() {
		
	}

}
