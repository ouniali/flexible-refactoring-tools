package BeneEvent;

import org.eclipse.mylyn.monitor.core.InteractionEvent;

public class MoveInvocation extends InteractionEvent{
	public MoveInvocation() {		
		super(InteractionEvent.Kind.COMMAND, "", "", "edu.ncsu.benefactor.move");
	}
}
