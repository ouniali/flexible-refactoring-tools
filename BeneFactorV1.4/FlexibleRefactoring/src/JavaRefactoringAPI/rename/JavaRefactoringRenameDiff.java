package JavaRefactoringAPI.rename;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.internal.corext.refactoring.rename.JavaRenameProcessor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.mylyn.internal.monitor.ui.MonitorUiPlugin;
import org.eclipse.mylyn.monitor.core.InteractionEvent;

import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;

import util.ASTUtil;
import util.ICompilationUnitUtil;

import animation.autoedition.AtomicEdition;
import animation.autoedition.MultiFileEdition;
import animation.autoedition.SingleFileEdition;
import animation.autoedition.ui.ScalingBar;
import animation.change.AutoEditionVisitor;
import animation.change.ChangeAnalyzer;
import animation.change.ChangeAnalyzer.*;

import ASTree.CUHistory.CompilationUnitHistoryRecord;
import Rename.ASTChangeName;
import Rename.NamesInJavaProject;
import Rename.NamesInPackage;

import compare.SourceDiff;

public class JavaRefactoringRenameDiff extends JavaReafactoringRenameBase {

	ASTChangeName first_dec_change;
	ASTChangeName last_dec_change;
	List<ASTChangeName> dec_changes;
	String bindingKey;

	private JavaRefactoringRenameDiff(ICompilationUnit u, int l,
			String oN, String nN,
			List<ASTChangeName> changes) throws Exception 
	{
		super(u, l, oN, nN);
		dec_changes = changes;
		first_dec_change = dec_changes.get(0);
		last_dec_change = dec_changes.get(dec_changes.size()-1);
		bindingKey = first_dec_change.getOldNameBindingKey();
	}
	
	public static JavaRefactoringRenameDiff create(ICompilationUnit u, int l,
			List<ASTChangeName> changes, String nN) throws Exception
	{
		if(changes.size() < 1)
			return null;
		String oN = changes.get(0).getOldName();
		return new JavaRefactoringRenameDiff(u, l, oN, nN, changes);	
	}
	
	@SuppressWarnings("restriction")
	@Override
	protected void performRefactoring(IProgressMonitor pm) throws Exception {
		
		SubMonitor monitor = SubMonitor.convert(pm,"Performing Rename Refactoring",6);
		RenameRefactoring refactoring;
		Name name;
		name = new NamesInPackage(ASTUtil.getContainingPackage(getICompilationUnit()))
		.getNameOfBinding(bindingKey);
		if(name != null)
		{
			IJavaElement element = name.resolveBinding().getJavaElement();
			element = correctElement(element, name);
			JavaRenameProcessor processor = getRenameProcessor(element);
			processor.setNewElementName(getNewName());
			refactoring = new RenameRefactoring(processor);
			refactoring.checkInitialConditions(monitor.newChild(1));
			refactoring.checkFinalConditions(monitor.newChild(1));
			Change change = refactoring.createChange(monitor.newChild(1));
			//showPreview(change);
			Change undo = change.perform(monitor.newChild(1));
			this.setUndo(undo);
		}

		monitor.done();
	}
	

	@Override
	protected void performCodeRecovery(IProgressMonitor pm) throws Exception 
	{	
		SubMonitor monitor = SubMonitor.convert(pm,"Recovering Code",2);
		String source = first_dec_change.getOldCompilationUnitRecord().getSourceCode();	
		source = performDiffs(source, getChangesAfterDeclarationRenamed());
		ICompilationUnitUtil.UpdateICompilationUnitWithoutCommit(this.getICompilationUnit(),source, monitor.newChild(1));
		monitor.done();
	}

	private List<SourceDiff> getChangesAfterDeclarationRenamed()
	{
		List<SourceDiff> diffs = new ArrayList<SourceDiff>();
		CompilationUnitHistoryRecord start = last_dec_change.getNewCompilationUnitRecord();
		CompilationUnitHistoryRecord end = start.getAllHistory().getMostRecentRecord();
		for(CompilationUnitHistoryRecord current = end; current != start; current = current.getPreviousRecord())
			diffs.add(0, current.getSourceDiff());
		return diffs;
	}
	
	private String performDiffs(String source, List<SourceDiff> diffs)
	{
		for(SourceDiff diff : diffs)
			source = diff.performChange(source);
		return source;
	}
	
	

	@Override
	public void postProcess() {
		MonitorUiPlugin.getDefault().
			notifyInteractionObserved(InteractionEvent.makeCommand(event_id+".rename", "rename"));
	}
	
	private void showPreview(Change change) throws Exception
	{
		List results = new ChangeAnalyzer(change, new AutoEditionVisitorStrategy()).getResults();
		new MultiFileEdition(results).play();
		Thread.sleep(Integer.MAX_VALUE);
	}

}
