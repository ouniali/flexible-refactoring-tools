package UserAction;

import org.eclipse.mylyn.monitor.core.IInteractionEventListener;
import org.eclipse.mylyn.monitor.core.InteractionEvent;
import org.eclipse.mylyn.monitor.core.InteractionEvent.Kind;

public class CopyEventListener implements IInteractionEventListener{

	@Override
	public void interactionObserved(InteractionEvent event) {
		// TODO Auto-generated method stub
		Kind k = event.getKind();
		System.out.println(k.toString());
	}

	@Override
	public void startMonitoring() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopMonitoring() {
		// TODO Auto-generated method stub
		
	}

}
