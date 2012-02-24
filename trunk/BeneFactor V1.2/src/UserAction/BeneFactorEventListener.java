package UserAction;

import org.eclipse.mylyn.monitor.core.IInteractionEventListener;
import org.eclipse.mylyn.monitor.core.InteractionEvent;

import JavaRefactoringAPI.JavaRefactoring;

public class BeneFactorEventListener implements IInteractionEventListener{

	@Override
	public void interactionObserved(InteractionEvent event) {
		String id = event.getOriginId();
		if(!id.equals(JavaRefactoring.event_id))
			return;
		System.out.println(id);
		
	}

	@Override
	public void startMonitoring() {
		
	}

	@Override
	public void stopMonitoring() {
		
	}

}
