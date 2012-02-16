package UserAction;

import org.eclipse.mylyn.monitor.core.IInteractionEventListener;
import org.eclipse.mylyn.monitor.core.InteractionEvent;

public class CutEventListener implements IInteractionEventListener{

	@Override
	public void interactionObserved(InteractionEvent event) {
		if(!event.getOriginId().equals("org.eclipse.ui.edit.cut"))
			return;
		System.out.println("cut");
	}

	@Override
	public void startMonitoring() {
		
	}

	@Override
	public void stopMonitoring() {
		
	}

}
