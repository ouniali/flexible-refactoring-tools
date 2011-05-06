package flexiblerefactoring.actions;



import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;


import ASTree.*;

public class GetInfo extends AbstractHandler {

	private ProjectHistoryCollector historyCollector;
	
	public GetInfo ()
	{
		super();
		historyCollector = new ProjectHistoryCollector();
		
	}
	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		return null;
	}

	/**
	 * Reads a ICompilationUnit and creates the AST DOM for manipulating the
	 * Java source file
	 * 
	 * @param unit
	 * @return
	 */

	
}
