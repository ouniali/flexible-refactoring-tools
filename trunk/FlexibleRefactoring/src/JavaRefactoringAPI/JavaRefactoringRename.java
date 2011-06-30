package JavaRefactoringAPI;

import java.util.ArrayList;

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
	ICompilationUnit unit;
	IJavaProject project;
	
	ArrayList<Name> namesInProject;
	boolean renamedBindingKey;
	
	public JavaRefactoringRename(String keyBefore,String keyAfter, String oN, String nN)
	{
		bindingKeyBeforeDeclarationChange = keyBefore;
		bindingKeyAfterDeclarationChange = keyAfter;
		oldName = oN;
		newName = nN;
	}
	
	@Override
	public void run() {
		try{
			performCodeRecovery();
			performRefactoring();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	@SuppressWarnings("restriction")
	public void setEnvironment(ICompilationUnit u) {
		unit = u; 
		project = u.getJavaProject();
	}

	@SuppressWarnings("restriction")
	@Override
	public void performRefactoring() throws Exception {
		NullProgressMonitor monitor = new NullProgressMonitor();
		RenameRefactoring refactoring;
		ArrayList<Name> names = new NamesInJavaProject(project).getNamesOfBindingInJavaProject(bindingKeyBeforeDeclarationChange);	
		
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
			change.perform(monitor);
		}
	}

	@Override
	public boolean checkPreconditions() {

		return true;
	}

	@Override
	public boolean checkPostconditions() {
		return true;
	}
	
	
	@SuppressWarnings("restriction")
	protected void performCodeRecovery() throws Exception
	{
		NullProgressMonitor monitor = new NullProgressMonitor();	
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
