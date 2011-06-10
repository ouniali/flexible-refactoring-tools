package JavaRefactoringAPI;

import java.util.ArrayList;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.ui.refactoring.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.ltk.core.refactoring.participants.*;
import org.eclipse.jdt.internal.corext.refactoring.rename.*;
import org.eclipse.jdt.internal.corext.util.*;

import Rename.SimpleNamesInCompilationUnit;

import ASTree.ASTreeManipulationMethods;



public class JavaRenameRefactoring extends JavaRefactoring{

	static final int flag = RenameSupport.UPDATE_REFERENCES|RenameSupport.UPDATE_GETTER_METHOD|RenameSupport.UPDATE_SETTER_METHOD;
	
	final String bindingKey;
	final String newName;
	IJavaElement element;
	ICompilationUnit unit;
	RenameRefactoring refactoring;
	
	public JavaRenameRefactoring(String bin, String n)
	{
		bindingKey = bin;
		newName = n;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		performRefactoring();
	}

	@Override
	@SuppressWarnings("restriction")
	public void setEnvironment(ICompilationUnit u) {
		// TODO Auto-generated method stub
		unit = u; 
		CompilationUnit tree = ASTreeManipulationMethods.parseICompilationUnit(u);
		ArrayList<SimpleName> names = new SimpleNamesInCompilationUnit(tree).getSimpleNamesOfBindingInCompilatioUnit(bindingKey);
		if(!names.isEmpty())
			element = names.get(0).resolveBinding().getJavaElement();
		try{	
			JavaRenameProcessor processor  = getRenameProcessor(element);
			processor.setNewElementName(newName);
			refactoring = new RenameRefactoring(processor);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void performRefactoring() {
		NullProgressMonitor monitor = new NullProgressMonitor();
		try{
			refactoring.checkInitialConditions(monitor);
			refactoring.checkFinalConditions(monitor);
			Change change = refactoring.createChange(monitor);
			change.perform(monitor);
		}catch (Exception e)
		{
			e.printStackTrace();
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
