package UserAction;

import org.eclipse.mylyn.monitor.core.IInteractionEventListener;
import org.eclipse.mylyn.monitor.core.InteractionEvent;
import org.eclipse.mylyn.monitor.core.InteractionEvent.Kind;
import org.eclipse.mylyn.monitor.core.IInteractionEventListener;

public class CopyEventListener implements IInteractionEventListener {


	@Override
	public void interactionObserved(InteractionEvent event) {
		// TODO Auto-generated method stub
		if(!event.getOriginId().equals("org.eclipse.ui.edit.copy"))
			return;
		System.out.println("copy");
	}

	@Override
	public void startMonitoring() {
	
	}

	@Override
	public void stopMonitoring() {
	}



}
