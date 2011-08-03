package JavaRefactoringAPI;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.internal.corext.refactoring.structure.*;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.*;

@SuppressWarnings("restriction")
public class JavaRefactoringMoveConstant extends JavaRefactoring {

	MoveRefactoring moveRefactoring;
	MoveStaticMembersProcessor moveProcessor;
	static int moveit = 0;
	public JavaRefactoringMoveConstant(ICompilationUnit u, int l, IMarker m) 
	{
		super(u, l, m);
	}

	@Override
	protected void performRefactoring() throws Exception 
	{
		NullProgressMonitor monitor = new NullProgressMonitor();
		IMember[] members = getMembersToBeMoved();
		ICompilationUnit unit = getICompilationUnit();
		IJavaProject project = unit.getJavaProject();
		moveProcessor = new MoveStaticMembersProcessor(members, JavaPreferencesSettings.getCodeGenerationSettings(project));
		moveRefactoring = new MoveRefactoring(moveProcessor);
		
		unit.becomeWorkingCopy(monitor);
		RefactoringStatus initStatus = moveRefactoring.checkInitialConditions(monitor);
		RefactoringStatus finalStatus = moveRefactoring.checkFinalConditions(monitor);
		Change change = moveRefactoring.createChange(monitor);
		Change undo = change.perform(monitor);
		setUndo(undo);
		unit.commitWorkingCopy(true, monitor);
		unit.discardWorkingCopy();	
		
		moveit = 2;
	}

	@Override
	protected void performCodeRecovery() throws Exception 
	{
		
	}

	@Override
	public int getRefactoringType() 
	{
		return JavaRefactoringType.MOVE_CONSTANT;
	}
	
	private IMember[] getMembersToBeMoved()
	{
		return null;
	}
	

}
