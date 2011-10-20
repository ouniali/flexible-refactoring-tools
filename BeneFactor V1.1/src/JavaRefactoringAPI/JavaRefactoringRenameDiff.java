package JavaRefactoringAPI;

import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.internal.corext.refactoring.rename.JavaRenameProcessor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;

import compare.SourceDiff;

import ASTree.CompilationUnitHistoryRecord;
import ASTree.CompilationUnitManipulationMethod;
import Rename.ASTNameChangeInformation;
import Rename.NamesInJavaProject;

public class JavaRefactoringRenameDiff extends JavaRefactoring {

	ASTNameChangeInformation declarationNameChange;
	String bindingKey;
	IJavaProject project;
	String newName;
	String oldName;

	public JavaRefactoringRenameDiff(ICompilationUnit u, int l, IMarker m,
			ASTNameChangeInformation decChange, String nN) 
	{
		super(u, l, m);
		declarationNameChange = decChange;
		bindingKey = decChange.getOldNameBindingKey();
		project = u.getJavaProject(); 
		newName = nN;
		oldName = decChange.getOldName();
	}

	
	public String getOldName()
	{
		return this.oldName;
	}
	public String getNewName()
	{
		return this.newName;
	}
	
	@SuppressWarnings("restriction")
	@Override
	protected void performRefactoring() throws Exception {
		// TODO Auto-generated method stub
		NullProgressMonitor monitor = new NullProgressMonitor();
		RenameRefactoring refactoring;
		ArrayList<Name> names = new NamesInJavaProject(project).getNamesOfBindingInJavaProject(bindingKey);	
		ICompilationUnit unit = this.getICompilationUnit();
		if(!names.isEmpty())
		{
			unit.becomeWorkingCopy(monitor);
			IJavaElement element = names.get(0).resolveBinding().getJavaElement();
			//new way to get element
			int name_start = names.get(0).getStartPosition();
			int name_length = names.get(0).getLength();
			element = unit.codeSelect(name_start, name_length)[0];
			
			JavaRenameProcessor processor = JavaRefactoringRename.getRenameProcessor(element);
			processor.setNewElementName(newName);
			refactoring = new RenameRefactoring(processor);
			refactoring.checkInitialConditions(monitor);
			refactoring.checkFinalConditions(monitor);
			Change change = refactoring.createChange(monitor);
			Change undo = change.perform(monitor);
			this.setUndo(undo);
			unit.commitWorkingCopy(true, monitor);
			unit.discardWorkingCopy();
		}

	}

	@Override
	protected void performCodeRecovery() throws Exception {
		
		String source = declarationNameChange.getOldCompilationUnitRecord().getSourceCode();
		Stack<SourceDiff> diffs = new Stack<SourceDiff>();
		CompilationUnitHistoryRecord startRecord = declarationNameChange.getNewCompilationUnitRecord();
		CompilationUnitHistoryRecord endRecord = startRecord.getAllHistory().getMostRecentRecord();
		CompilationUnitHistoryRecord currentRecord = endRecord;

		while (currentRecord != startRecord && currentRecord != null) 
		{
			diffs.push(currentRecord.getSourceDiff());
			currentRecord = currentRecord.getPreviousRecord();
		}
		
		source = startRecord.getSourceDiff().skipChange(source);
		
		while(!diffs.isEmpty())
		{
			SourceDiff diff = diffs.pop();
			source = diff.performChange(source);
		}
	
		CompilationUnitManipulationMethod.UpdateICompilationUnit(this.getICompilationUnit(),source);

	}

	@Override
	public int getRefactoringType() {
		// TODO Auto-generated method stub
		return JavaRefactoringType.RENAME;
	}

}
