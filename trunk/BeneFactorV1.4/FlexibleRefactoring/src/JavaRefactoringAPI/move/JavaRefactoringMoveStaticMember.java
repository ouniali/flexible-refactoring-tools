package JavaRefactoringAPI.move;

import movestaticmember.ASTChangeAddStaticMember;
import movestaticmember.ASTChangeDeleteStaticMember;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveStaticMembersProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.MoveRefactoring;
import org.eclipse.mylyn.internal.monitor.ui.MonitorUiPlugin;
import org.eclipse.mylyn.monitor.core.InteractionEvent;

import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;



@SuppressWarnings("restriction")

public class JavaRefactoringMoveStaticMember extends JavaRefactoring {

	MoveRefactoring moveRefactoring;
	MoveStaticMembersProcessor moveProcessor;
	ASTChangeDeleteStaticMember deleteChange;
	ASTChangeAddStaticMember addChange;
	
	public JavaRefactoringMoveStaticMember(ICompilationUnit u, int l, ASTChangeDeleteStaticMember delete, ASTChangeAddStaticMember add) throws Exception 
	{
		super(u, l);
		deleteChange = delete;
		addChange = add;
	}

	@Override
	protected void performRefactoring(IProgressMonitor pm) throws Exception 
	{
		SubMonitor monitor = SubMonitor.convert(pm,"Performing Move Refactoring",6);
		
		IMember[] members = getMembersToBeMoved();
		ICompilationUnit unit = getICompilationUnit();
		IJavaProject project = unit.getJavaProject();
		moveProcessor = new MoveStaticMembersProcessor(members, JavaPreferencesSettings.getCodeGenerationSettings(project));
		moveProcessor.setDestinationTypeFullyQualifiedName(addChange.getDestinationTypeFullName());
		moveRefactoring = new MoveRefactoring(moveProcessor);
		

		unit.becomeWorkingCopy(monitor.newChild(1));
		RefactoringStatus initStatus = moveRefactoring.checkInitialConditions(monitor.newChild(1));
		RefactoringStatus finalStatus = moveRefactoring.checkFinalConditions(monitor.newChild(1));
		Change change = moveRefactoring.createChange(monitor.newChild(1));
		Change undo = change.perform(monitor.newChild(1));
		setUndo(undo);
		unit.commitWorkingCopy(true, monitor.newChild(1));
		unit.discardWorkingCopy();	
		
		monitor.done();
	}

	@Override
	protected void performCodeRecovery(IProgressMonitor pm) throws Exception 
	{
		SubMonitor monitor = SubMonitor.convert(pm,"Recovering Code",2);
		deleteChange.recoverICompilationUnitToOldRecord(monitor.newChild(1));
		addChange.recoverICompilationUnitToOldRecord(monitor.newChild(1));
		monitor.done();
	}

	@Override
	public int getRefactoringType() 
	{
		return JavaRefactoringType.MOVE_STATIC;
	}
	
	private IMember[] getMembersToBeMoved() throws Exception
	{
		IMember mem = deleteChange.getMovedStaticField();
		return new IMember[]{mem};
	}





	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postProcess() {
		// TODO Auto-generated method stub
		MonitorUiPlugin.getDefault().notifyInteractionObserved(InteractionEvent.makeCommand(event_id, "move"));
	}
	

}
