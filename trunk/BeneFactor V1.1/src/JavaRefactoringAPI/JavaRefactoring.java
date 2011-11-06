package JavaRefactoringAPI;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.ltk.core.refactoring.Change;

import compilation.RefactoringChances;
import ASTree.CompilationUnitHistory;

public abstract class JavaRefactoring extends Job{

	private ICompilationUnit unit;
	private int line;
	private IMarker marker;
	private Change undo;
	protected long WAIT_TIME = 2000;
	
	
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
		
			preProcess();
			performCodeRecovery(progress.newChild(49));
			
			performRefactoring(progress.newChild(50));	
			
			RefactoringChances.clearRefactoringChances();
			postProcess();
			
			progress.worked(1);
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
    abstract public void preProcess();
	abstract public void postProcess();
	
}
