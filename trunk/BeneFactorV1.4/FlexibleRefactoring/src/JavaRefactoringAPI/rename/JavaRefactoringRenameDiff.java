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

import animation.autoedition.AtomicEdition;
import animation.autoedition.MultiFileEdition;
import animation.autoedition.SingleFileEdition;
import animation.autoedition.ui.ScalingBar;
import animation.change.AutoEditionVisitor;
import animation.change.ChangeAnalyzer;
import animation.change.ChangeAnalyzer.*;

import ASTree.CompilationUnitHistoryRecord;
import ASTree.CompilationUnitManipulationMethod;
import Rename.ASTNameChangeInformation;
import Rename.NamesInJavaProject;
import Rename.NamesInPackage;

import compare.SourceDiff;

public class JavaRefactoringRenameDiff extends JavaReafactoringRenameBase {

	ASTNameChangeInformation first_dec_change;
	ASTNameChangeInformation last_dec_change;
	List<ASTNameChangeInformation> dec_changes;
	String bindingKey;

	private JavaRefactoringRenameDiff(ICompilationUnit u, int l, IMarker m,
			String oN, String nN,
			List<ASTNameChangeInformation> changes) throws Exception 
	{
		super(u, l, m, oN, nN);
		dec_changes = changes;
		first_dec_change = dec_changes.get(0);
		last_dec_change = dec_changes.get(dec_changes.size()-1);
		bindingKey = first_dec_change.getOldNameBindingKey();
	}
	
	public static JavaRefactoringRenameDiff create(ICompilationUnit u, int l, IMarker m,
			List<ASTNameChangeInformation> changes, String nN) throws Exception
	{
		String oN = changes.get(0).getOldName();
		return new JavaRefactoringRenameDiff(u, l, m, oN, nN, changes);
		
	}
	
	@SuppressWarnings("restriction")
	@Override
	protected void performRefactoring(IProgressMonitor pm) throws Exception {
		
		SubMonitor monitor = SubMonitor.convert(pm,"Performing Rename Refactoring",6);
		RenameRefactoring refactoring;
		Name name;
		name = new NamesInPackage(ASTUtil.getContainingPackage(getICompilationUnit()))
		.getNameOfBinding(bindingKey);
		ICompilationUnit unit = this.getICompilationUnit();
		if(name != null)
		{
			IJavaElement element = name.resolveBinding().getJavaElement();
			
			//new way to get element
			IJavaElement entire_element = element;
			try{
				int name_start = name.getStartPosition();
				int name_length = name.getLength();
				IJavaElement[] primary_elements = unit.codeSelect(name_start, name_length);
				element = primary_elements[0];
			} catch (Exception e){
				element = entire_element;
			}
			//new way to get element
			
			JavaRenameProcessor processor = getRenameProcessor(element);
			processor.setNewElementName(getNewName());
			refactoring = new RenameRefactoring(processor);
			refactoring.checkInitialConditions(monitor.newChild(1));
			refactoring.checkFinalConditions(monitor.newChild(1));
			Change change = refactoring.createChange(monitor.newChild(1));
			
			//add preview here
			
		/*	ArrayList results = new ChangeAnalyzer(change, new AutoEditionVisitorStrategy()).getResults();
			new MultiFileEdition(results).play();
			Thread.sleep(Integer.MAX_VALUE);*/
			
			//
			
			
			//System.out.println("add preview here");			
			Change undo = change.perform(monitor.newChild(1));
			this.setUndo(undo);
		}

		monitor.done();
	}
	

	@Override
	protected void performCodeRecovery(IProgressMonitor pm) throws Exception {
		
		SubMonitor monitor = SubMonitor.convert(pm,"Recovering Code",2);
		
		if(first_dec_change == null)
			return;
			
		String source = first_dec_change.getOldCompilationUnitRecord().getSourceCode();
		ArrayList<CompilationUnitHistoryRecord> records = new ArrayList<CompilationUnitHistoryRecord>();
		CompilationUnitHistoryRecord startRecord = first_dec_change.getOldCompilationUnitRecord();
		CompilationUnitHistoryRecord endRecord = startRecord.getAllHistory().getMostRecentRecord();
		CompilationUnitHistoryRecord currentRecord = endRecord;

		
		while (currentRecord != startRecord && currentRecord != null) 
		{
			records.add(0, currentRecord);
			currentRecord = currentRecord.getPreviousRecord();
		}
		records.add(0, currentRecord);
		
		boolean[] does_skips = new boolean[records.size()];
		for(int i = 1; i< does_skips.length; i++)
			does_skips[i] = false;
		
		for(ASTNameChangeInformation change : dec_changes)
		{
			int start = records.indexOf(change.getOldCompilationUnitRecord());
			int end = records.indexOf(change.getNewCompilationUnitRecord());
			
			for(int i = start ; i<= end; i++)
				does_skips[i] = true;
		}
		
		for(int i = 1 ; i< records.size(); i++)
		{
			SourceDiff d = records.get(i).getSourceDiff();
			if(does_skips[i])
				source = d.skipChange(source);
			else 
				source = d.performChange(source);
		}
		
		monitor.worked(1);
	

		CompilationUnitManipulationMethod.UpdateICompilationUnitWithoutCommit(this.getICompilationUnit(),source, monitor.newChild(1));

		monitor.done();
	}


	@Override
	public void preProcess() {
		
	}


	@Override
	public void postProcess() {
		MonitorUiPlugin.getDefault().notifyInteractionObserved(InteractionEvent.makeCommand(event_id+".rename", "rename"));
	}

}