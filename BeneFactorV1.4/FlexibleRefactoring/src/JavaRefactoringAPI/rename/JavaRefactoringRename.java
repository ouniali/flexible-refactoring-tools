package JavaRefactoringAPI.rename;

import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.internal.corext.refactoring.rename.JavaRenameProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.MethodChecks;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameCompilationUnitProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameEnumConstProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameFieldProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameJavaProjectProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameLocalVariableProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameNonVirtualMethodProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenamePackageProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameSourceFolderProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameTypeParameterProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameTypeProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameVirtualMethodProcessor;
import org.eclipse.jdt.internal.corext.util.JdtFlags;
import org.eclipse.jdt.ui.refactoring.RenameSupport;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.mylyn.internal.monitor.ui.MonitorUiPlugin;
import org.eclipse.mylyn.monitor.core.InteractionEvent;

import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;

import util.ASTUtil;

import Rename.NamesInJavaProject;
import Rename.NamesInPackage;



public class JavaRefactoringRename extends JavaReafactoringRenameBase{

	
	
	final String bindingKeyBeforeDeclarationChange;
	final String bindingKeyAfterDeclarationChange;
	
	IJavaElement element;
	
	ArrayList<Name> namesInProject;
	boolean renamedBindingKey;
	
	public JavaRefactoringRename(ICompilationUnit u,int l, String keyBefore,String keyAfter, String oN, String nN) throws Exception
	{
		super(u, l, oN, nN);
		assert(keyBefore!= null && keyAfter != null);
		bindingKeyBeforeDeclarationChange = keyBefore;
		bindingKeyAfterDeclarationChange = keyAfter;
	}	
	
	
	
	
	@SuppressWarnings("restriction")
	@Override
	public void performRefactoring(IProgressMonitor pm) throws Exception {
		
		SubMonitor monitor = SubMonitor.convert(pm,"Performing Rename Refactoring",6);
		
		RenameRefactoring refactoring;
		Name name; 
		name = new NamesInPackage(ASTUtil.getContainingPackage(getICompilationUnit()))
		.getNameOfBinding(bindingKeyBeforeDeclarationChange);
		if(name != null)
		{
			IJavaElement element = name.resolveBinding().getJavaElement();
			element = correctElement(element, name);
			JavaRenameProcessor processor = getRenameProcessor(element);
			processor.setNewElementName(getNewName());
			refactoring = new RenameRefactoring(processor);
			RefactoringStatus  initialStatus = refactoring.checkInitialConditions(monitor.newChild(1));
			RefactoringStatus  finalStatus = refactoring.checkFinalConditions(monitor.newChild(1));
			Change change = refactoring.createChange(monitor.newChild(1));
			Change undo = change.perform(monitor.newChild(1));
			this.setUndo(undo);
		}
		
		monitor.done();
	}
	
	@SuppressWarnings("restriction")
	@Override
	protected void performCodeRecovery(IProgressMonitor pm) throws Exception
	{
		SubMonitor monitor = SubMonitor.convert(pm,"Recovering Code", 6);	
		ICompilationUnit unit = this.getICompilationUnit();
		if(!bindingKeyBeforeDeclarationChange.equals(bindingKeyAfterDeclarationChange))
		{		
			Name name = new NamesInJavaProject(unit.getJavaProject()).getANameWithBinding(bindingKeyAfterDeclarationChange);
			if(name != null)
			{				
				IJavaElement element = name.resolveBinding().getJavaElement();		
				element = correctElement(element, name);
				JavaRenameProcessor processor = getRenameProcessor(element);
				processor.setNewElementName(getOldName());
				RenameRefactoring recoverRefactoring = new RenameRefactoring(processor);
				RefactoringStatus  initialStatus = recoverRefactoring.checkInitialConditions(monitor.newChild(1));
				RefactoringStatus  finalStatus = recoverRefactoring.checkFinalConditions(monitor.newChild(1));
				recoverRefactoring.createChange(monitor.newChild(1)).perform(monitor.newChild(1));
			}	
		}
		monitor.done();
	}



	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postProcess() {
		// TODO Auto-generated method stub
		MonitorUiPlugin.getDefault().notifyInteractionObserved(InteractionEvent.makeCommand(event_id + ".rename", "rename"));
	}


}
