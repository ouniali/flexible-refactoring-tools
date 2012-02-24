package JavaRefactoringAPI;

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

import ASTree.ASTreeManipulationMethods;
import Rename.NamesInJavaProject;
import Rename.NamesInPackage;



public class JavaRefactoringRename extends JavaRefactoring{

	static final int flag = RenameSupport.UPDATE_REFERENCES|RenameSupport.UPDATE_GETTER_METHOD|RenameSupport.UPDATE_SETTER_METHOD;
	
	
	final String bindingKeyBeforeDeclarationChange;
	final String bindingKeyAfterDeclarationChange;
	final String oldName;
	final String newName;
	
	IJavaElement element;
	IJavaProject project;
	
	ArrayList<Name> namesInProject;
	boolean renamedBindingKey;
	
	public JavaRefactoringRename(ICompilationUnit u,int l, IMarker m,String keyBefore,String keyAfter, String oN, String nN)
	{
		super(u, l, m);
		assert(keyBefore!= null && keyAfter != null);
		bindingKeyBeforeDeclarationChange = keyBefore;
		bindingKeyAfterDeclarationChange = keyAfter;
		oldName = oN;
		newName = nN;
		project = u.getJavaProject(); 
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
	public void performRefactoring(IProgressMonitor pm) throws Exception {
		
		SubMonitor monitor = SubMonitor.convert(pm,"Performing Rename Refactoring",6);
		
		RenameRefactoring refactoring;
		Name name; 
		name = new NamesInPackage(ASTreeManipulationMethods.getContainingPackage(getICompilationUnit()))
		.getNameOfBinding(bindingKeyBeforeDeclarationChange);
		ICompilationUnit unit = this.getICompilationUnit();
		if(name != null)
		{
			IJavaElement element = name.resolveBinding().getJavaElement();
			IJavaElement entire_element = element;
			try{
				int name_start = name.getStartPosition();
				int name_length = name.getLength();
				IJavaElement[] primary_elements = unit.codeSelect(name_start, name_length);
				element = primary_elements[0];
			} catch (Exception e){
				element = entire_element;
			}
				
			JavaRenameProcessor processor = getRenameProcessor(element);
			processor.setNewElementName(newName);
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
				processor.setNewElementName(oldName);
				RenameRefactoring recoverRefactoring = new RenameRefactoring(processor);
				RefactoringStatus  initialStatus = recoverRefactoring.checkInitialConditions(monitor.newChild(1));
				RefactoringStatus  finalStatus = recoverRefactoring.checkFinalConditions(monitor.newChild(1));
				recoverRefactoring.createChange(monitor.newChild(1)).perform(monitor.newChild(1));
			}
			
		}
		
		monitor.done();
	}

	@Override
	public int getRefactoringType() {
		// TODO Auto-generated method stub
		return JavaRefactoringType.RENAME;
	}
	
	
	@SuppressWarnings("restriction")
	public static JavaRenameProcessor getRenameProcessor(IJavaElement element) throws Exception
	{
		int eleType = element.getElementType();				
		
		switch(eleType)
		{
		case IJavaElement.COMPILATION_UNIT:			
			ICompilationUnit unit = (ICompilationUnit) element;
			return new RenameCompilationUnitProcessor(unit);

		case IJavaElement.FIELD:
			IField field = (IField)element;
			if (JdtFlags.isEnum(field))
				return new RenameEnumConstProcessor(field);
			else {
				final RenameFieldProcessor RFprocessor= new RenameFieldProcessor(field);
				RFprocessor.setRenameGetter((flag & RenameSupport.UPDATE_GETTER_METHOD) != 0);
				RFprocessor.setRenameSetter((flag & RenameSupport.UPDATE_SETTER_METHOD) != 0);
				return RFprocessor;
			}

		case IJavaElement.JAVA_PROJECT:			
			IJavaProject project = (IJavaProject) element;
			return new RenameJavaProjectProcessor(project);
	
		case IJavaElement.LOCAL_VARIABLE: 	
			ILocalVariable variable = (ILocalVariable) element;
			RenameLocalVariableProcessor LVProcessor = new RenameLocalVariableProcessor(variable);
			LVProcessor.setUpdateReferences((flag & RenameSupport.UPDATE_REFERENCES) != 0);
			return LVProcessor;
			
		case IJavaElement.METHOD:
			final IMethod method= (IMethod)element;
			JavaRenameProcessor MProcessor;
			if (MethodChecks.isVirtual(method)) {
				MProcessor= new RenameVirtualMethodProcessor(method);
			} else {
				MProcessor= new RenameNonVirtualMethodProcessor(method);
			}
            return MProcessor;
             
		case IJavaElement.PACKAGE_FRAGMENT:	
			IPackageFragment fragment = (IPackageFragment) element;
			return new RenamePackageProcessor(fragment);
			
		case IJavaElement.PACKAGE_FRAGMENT_ROOT:
			IPackageFragmentRoot root = (IPackageFragmentRoot) element;
			return new RenameSourceFolderProcessor(root);
			
		case IJavaElement.TYPE_PARAMETER:
			ITypeParameter parameter = (ITypeParameter) element;
			RenameTypeParameterProcessor TPProcessor= new RenameTypeParameterProcessor(parameter);
			TPProcessor.setUpdateReferences((flag & RenameSupport.UPDATE_REFERENCES) != 0);
			return TPProcessor;
			
		case IJavaElement.TYPE:			
			IType type = (IType)element;
			return new RenameTypeProcessor(type);
		
		default:
			return null;
		}
	}



	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postProcess() {
		// TODO Auto-generated method stub
		MonitorUiPlugin.getDefault().notifyInteractionObserved(InteractionEvent.makeCommand(event_id, "rename"));
	}


}
