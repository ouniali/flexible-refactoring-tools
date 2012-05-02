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

import utitilies.UIUtil;

import compilation.RefactoringChances;
import ASTree.CompilationUnitHistory;

public abstract class JavaRefactoring extends Job{

	private ICompilationUnit unit;
	private int line;
	private IMarker marker;
	private Change undo;
	public static final String event_id = "org.eclipse.edu.ncsu.BeneFactor";
	
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
		
		try {
			ICompilationUnit unit = this.getICompilationUnit();
			UIUtil.freezeEditor(UIUtil.getActiveJavaEditor());
			
			unit.becomeWorkingCopy(progress.newChild(1));
			
			preProcess();			
			performCodeRecovery(progress.newChild(49));		
			performRefactoring(progress.newChild(50));	
			postProcess();
			
			RefactoringChances.clearRefactoringChances();	
			
			progress.worked(1);
			unit.commitWorkingCopy(true, progress.newChild(1));
			unit.discardWorkingCopy();
			
			UIUtil.wakeUpEditor(UIUtil.getActiveJavaEditor());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			progress.done();
		}
		
		return Status.OK_STATUS;
	}

	public JavaRefactoring(ICompilationUnit u, int l, IMarker m) throws Exception
	{
		super("Java Refactoring Job");
		unit = u;
		line = l;
		marker = m;
	}
	public final ICompilationUnit getICompilationUnit() throws Exception
	{
		unit.makeConsistent(null);
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
	protected JavaUndoRefactoring getJavaUndoRefactoring() throws Exception
	{
		if(getUndo()!= null)
			return new JavaUndoRefactoring(getICompilationUnit(), getLineNumber(), getRefactoringType(), getUndo() );
		else
			return null;
	}
	
	abstract public int getRefactoringType();
    public abstract void preProcess() throws Exception;
	public abstract void postProcess() throws Exception;	
}
