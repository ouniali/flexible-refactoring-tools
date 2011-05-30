package JavaRefactoringAPI;

import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.ui.refactoring.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.ltk.core.refactoring.participants.*;
import org.eclipse.jdt.internal.corext.refactoring.rename.*;
import org.eclipse.jdt.internal.corext.util.*;



public class JavaRenameRefactoringAPI {

	static final int flag = RenameSupport.UPDATE_REFERENCES|RenameSupport.UPDATE_GETTER_METHOD|RenameSupport.UPDATE_SETTER_METHOD;
	
	@SuppressWarnings("restriction")
	public static void performRefactoring(IJavaElement element, String newName) throws Exception
	{	

		RefactoringCheckingStatusProgressMonitor myMonitor = new RefactoringCheckingStatusProgressMonitor();
		JavaRenameProcessor processor  = getRenameProcessor(element);
		processor.setNewElementName(newName);
		RenameRefactoring refactoring = new RenameRefactoring(processor);
		System.out.println(element);
		RefactoringStatus preStatus = refactoring.checkInitialConditions(myMonitor);
		if(preStatus.isOK())
			System.out.println("preconditions ok");
		RefactoringStatus postStatus = refactoring.checkFinalConditions(myMonitor);
		refactoring.createChange(myMonitor);
		System.out.println("d");
	
	}
	
	@SuppressWarnings("restriction")
	private static JavaRenameProcessor getRenameProcessor(IJavaElement element) throws CoreException
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
