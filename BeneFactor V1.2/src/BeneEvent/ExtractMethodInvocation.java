package BeneEvent;

import org.eclipse.mylyn.monitor.core.InteractionEvent;

public class ExtractMethodInvocation extends InteractionEvent{

	public ExtractMethodInvocation() {
		
		
		super(InteractionEvent.Kind.COMMAND, "", "", "edu.ncsu.benefactor.extractmethod");
	}

}
