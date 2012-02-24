package BeneEvent;

import org.eclipse.mylyn.monitor.core.InteractionEvent;

public class RenameInvocation extends InteractionEvent{

	public RenameInvocation() {
		super(InteractionEvent.Kind.COMMAND, "", "", "edu.ncsu.benefactor.rename");
	}

}
