package JavaRefactoringAPI.rename;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
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

import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringType;

public class JavaReafactoringRenameBase extends JavaRefactoring{

	private final String old_name;
	private final String new_name;
	private final int flag = 
			RenameSupport.UPDATE_REFERENCES|RenameSupport.UPDATE_GETTER_METHOD|RenameSupport.UPDATE_SETTER_METHOD;
	

	public JavaReafactoringRenameBase(ICompilationUnit u, int l, String oN, String nN)
			throws Exception {
		super(u, l);
		old_name = oN;
		new_name = nN;
	}

	public final String getOldName() {
		return old_name;
	}

	public final String getNewName() {
		return new_name;
	}


	@Override
	protected void performRefactoring(IProgressMonitor pm) throws Exception {}

	@Override
	protected void performCodeRecovery(IProgressMonitor pm) throws Exception {}

	@Override
	public final int getRefactoringType() {
		return JavaRefactoringType.RENAME;
	}

	@Override
	public void preProcess() throws Exception {}

	@Override
	public void postProcess() throws Exception {}
	
	protected IJavaElement correctElement(IJavaElement element, Name name)
	{
		IJavaElement original = element;
		try{
			int name_start = name.getStartPosition();
			int name_length = name.getLength();
			IJavaElement[] primary_elements = getICompilationUnit().
					codeSelect(name_start, name_length);
			element = primary_elements[0];
		} catch (Exception e){
			element = original;
		}
		
		return element;
	}
	@SuppressWarnings("restriction")
	protected JavaRenameProcessor getRenameProcessor(IJavaElement element) throws Exception
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
