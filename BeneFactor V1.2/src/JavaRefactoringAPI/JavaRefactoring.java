package JavaRefactoringAPI;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.mylyn.internal.monitor.ui.MonitorUiPlugin;
import org.eclipse.mylyn.monitor.core.InteractionEvent;
import org.eclipse.ui.IEditorInput;

import utitilies.UserInterfaceUtilities;

import compilation.RefactoringChances;
import ASTree.CompilationUnitHistory;

public abstract class JavaRefactoring extends Job{

	private ICompilationUnit unit;
	private int line;
	private IMarker marker;
	private Change undo;
	public static final String event_id = "edu.ncsu.BeneFactor";
	
	protected abstract void performRefactoring(IProgressMonitor pm) throws Exception;
	protected abstract void performCodeRecovery(IProgressMonitor pm) throws Exception;
	protected final void setUndo(Change u)
	{
		undo = u;
	}
	protected final Change getUndo()
	{
		return undo;
	}
	
	@Override
	public IStatus run(IProgressMonitor pm) {
		
		SubMonitor progress = SubMonitor.convert(pm, "Running refactoring", 100);
		ICompilationUnit unit = this.getICompilationUnit();
		try {
			UserInterfaceUtilities.freezeEditor(UserInterfaceUtilities.getActiveJavaEditor());
			
			unit.becomeWorkingCopy(progress.newChild(1));
			
			preProcess();			
			performCodeRecovery(progress.newChild(49));		
			performRefactoring(progress.newChild(50));	
			postProcess();
			
			RefactoringChances.clearRefactoringChances();	
			
			progress.worked(1);
			unit.commitWorkingCopy(true, progress.newChild(1));
			unit.discardWorkingCopy();
			
			UserInterfaceUtilities.wakeUpEditor(UserInterfaceUtilities.getActiveJavaEditor());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			progress.done();
		}
		
		return Status.OK_STATUS;
	}

	public JavaRefactoring(ICompilationUnit u, int l, IMarker m)
	{
		super("Java Refactoring Job");
		unit = u;
		line = l;
		marker = m;
	}
	public final ICompilationUnit getICompilationUnit()
	{
		try {
			unit.makeConsistent(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unit;
	}
	public final int getLineNumber()
	{
		return line;
	}
	
	public final IMarker getMarker()
	{
		return marker;
	}
	protected JavaUndoRefactoring getJavaUndoRefactoring()
	{
		if(getUndo()!= null)
			return new JavaUndoRefactoring(getICompilationUnit(), getLineNumber(), getRefactoringType(), getUndo() );
		else
			return null;
	}
	
	abstract public int getRefactoringType();
    public abstract void preProcess();
	public abstract void postProcess();	
}
