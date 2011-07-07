package JavaRefactoringAPI;

import java.util.ArrayList;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.ui.refactoring.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.ltk.core.refactoring.participants.*;
import org.eclipse.jdt.internal.core.JavaElement;
import org.eclipse.jdt.internal.corext.refactoring.rename.*;
import org.eclipse.jdt.internal.corext.util.*;

import Rename.*;

import ASTree.ASTreeManipulationMethods;



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
	
	@SuppressWarnings("restriction")
	@Override
	public void performRefactoring() throws Exception {
		NullProgressMonitor monitor = new NullProgressMonitor();
		RenameRefactoring refactoring;
		ArrayList<Name> names = new NamesInJavaProject(project).getNamesOfBindingInJavaProject(bindingKeyBeforeDeclarationChange);	
		ICompilationUnit unit = this.getICompilationUnit();
		if(!names.isEmpty())
		{
			unit.becomeWorkingCopy(monitor);
			IJavaElement element = names.get(0).resolveBinding().getJavaElement();
			JavaRenameProcessor processor = getRenameProcessor(element);
			processor.setNewElementName(newName);
			refactoring = new RenameRefactoring(processor);
			refactoring.checkInitialConditions(monitor);
			refactoring.checkFinalConditions(monitor);
			Change change = refactoring.createChange(monitor);
			Change undo = change.perform(monitor);
			this.setUndo(undo);
			unit.discardWorkingCopy();
		}
	}
	
	@SuppressWarnings("restriction")
	protected void performCodeRecovery() throws Exception
	{
		NullProgressMonitor monitor = new NullProgressMonitor();	
		ICompilationUnit unit = this.getICompilationUnit();
		if(!bindingKeyBeforeDeclarationChange.equals(bindingKeyAfterDeclarationChange))
		{		
			ArrayList<Name> names = new NamesInJavaProject(project).getNamesOfBindingInJavaProject(bindingKeyAfterDeclarationChange);
			if(!names.isEmpty())
			{
				unit.becomeWorkingCopy(monitor);
				IJavaElement element = names.get(0).resolveBinding().getJavaElement();
				JavaRenameProcessor processor = getRenameProcessor(element);
				processor.setNewElementName(oldName);
				RenameRefactoring recoverRefactoring = new RenameRefactoring(processor);
				recoverRefactoring.checkInitialConditions(monitor);
				recoverRefactoring.checkFinalConditions(monitor);
				recoverRefactoring.createChange(monitor).perform(monitor);
				unit.commitWorkingCopy(true, monitor);
				unit.makeConsistent(monitor);
			}
			
		}
	}

	@Override
	public int getRefactoringType() {
		// TODO Auto-generated method stub
		return JavaRefactoringType.RENAME;
	}
	
	
	@SuppressWarnings("restriction")
	private static JavaRenameProcessor getRenameProcessor(IJavaElement element) throws Exception
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


}
