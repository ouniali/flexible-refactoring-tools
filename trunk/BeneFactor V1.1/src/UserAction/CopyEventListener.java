package UserAction;

import org.eclipse.mylyn.monitor.core.IInteractionEventListener;
import org.eclipse.mylyn.monitor.core.InteractionEvent;
import org.eclipse.mylyn.monitor.core.InteractionEvent.Kind;
import org.eclipse.mylyn.monitor.core.IInteractionEventListener;

public class CopyEventListener implements IInteractionEventListener {


	@Override
	public void interactionObserved(InteractionEvent event) {
		// TODO Auto-generated method stub
		System.out.println("interactionObserved");
	}

	@Override
	public void startMonitoring() {
		// TODO Auto-generated method stub
		System.out.println("startMonitoring");
	}

	@Override
	public void stopMonitoring() {
		// TODO Auto-generated method stub
		System.out.println("stopMonitoring");
	}



}
