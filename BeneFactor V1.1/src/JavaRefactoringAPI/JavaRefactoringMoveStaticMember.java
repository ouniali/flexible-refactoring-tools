package JavaRefactoringAPI;

import movestaticmember.ASTChangeInformationAddStaticMember;
import movestaticmember.ASTChangeInformationDeleteStaticMember;

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
public class JavaRefactoringMoveStaticMember extends JavaRefactoring {

	MoveRefactoring moveRefactoring;
	MoveStaticMembersProcessor moveProcessor;
	ASTChangeInformationDeleteStaticMember deleteChange;
	ASTChangeInformationAddStaticMember addChange;
	
	public JavaRefactoringMoveStaticMember(ICompilationUnit u, int l, IMarker m, ASTChangeInformationDeleteStaticMember delete, ASTChangeInformationAddStaticMember add) 
	{
		super(u, l, m);
		deleteChange = delete;
		addChange = add;
	}

	@Override
	protected void performRefactoring() throws Exception 
	{
		NullProgressMonitor monitor = new NullProgressMonitor();
		IMember[] members = getMembersToBeMoved();
		ICompilationUnit unit = getICompilationUnit();
		IJavaProject project = unit.getJavaProject();
		moveProcessor = new MoveStaticMembersProcessor(members, JavaPreferencesSettings.getCodeGenerationSettings(project));
		moveProcessor.setDestinationTypeFullyQualifiedName(addChange.getDestinationTypeFullName());
		moveRefactoring = new MoveRefactoring(moveProcessor);
		
		unit.becomeWorkingCopy(monitor);
		RefactoringStatus initStatus = moveRefactoring.checkInitialConditions(monitor);
		RefactoringStatus finalStatus = moveRefactoring.checkFinalConditions(monitor);
		Change change = moveRefactoring.createChange(monitor);
		Change undo = change.perform(monitor);
		setUndo(undo);
		unit.commitWorkingCopy(true, monitor);
		unit.discardWorkingCopy();	
	}

	@Override
	protected void performCodeRecovery() throws Exception 
	{
		deleteChange.recoverICompilationUnitToOldRecord();
		addChange.recoverICompilationUnitToOldRecord();
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
	

}
