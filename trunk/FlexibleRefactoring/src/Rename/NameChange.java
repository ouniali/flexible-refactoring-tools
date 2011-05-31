package Rename;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.*;

import ASTree.ASTreeManipulationMethods;
public class NameChange {
	
	public static final int NOT_NAME_CHANGE = -1;
	
	public static final int VARIABLE_NAME_CHANGE_REFERENCE=0;
	public static final int VARIABLE_NAME_CHANGE_DECLARATION = 1;
	public static final int METHOD_NAME_CHANGE_INVOCATION=2;
	public static final int METHOD_NAME_CHANGE_DECLARATION=3;
	public static final int TYPE_NAME_CHANGE_REFERENCE = 4;
	public static final int TYPE_NAME_CHANGE_DECLARATION = 5;
	public static final int PACKAGE_NAME_CHANGE_REFERENCE = 6;
	public static final int PACKAGE_NAME_CHANGE_DECLARATION = 7;
		
	public static final int UNCERTAIN_NAME_CHANGE = 8;
	
	public static String getNameChangeTypeDescription(int nameChangeType)
	{
		switch(nameChangeType)
		{
		case VARIABLE_NAME_CHANGE_REFERENCE:
			return "VARIABLE_NAME_CHANGE_REFERENCE";
		case VARIABLE_NAME_CHANGE_DECLARATION:
			return "VARIABLE_NAME_CHANGE_DECLARATION";
		case METHOD_NAME_CHANGE_INVOCATION:
			return "METHOD_NAME_CHANGE_INVOCATION";
		case METHOD_NAME_CHANGE_DECLARATION:
			return "METHOD_NAME_CHANGE_DECLARATION";
		case TYPE_NAME_CHANGE_REFERENCE:
			return "TYPE_NAME_CHANGE_REFERENCE";
		case TYPE_NAME_CHANGE_DECLARATION:
			return "TYPE_NAME_CHANGE_DECLARATION";
		case PACKAGE_NAME_CHANGE_REFERENCE:
			return "PACKAGE_NAME_CHANGE_REFERENCE";
		case PACKAGE_NAME_CHANGE_DECLARATION:
			return "PACKAGE_NAME_CHANGE_DECLARATION";
		case UNCERTAIN_NAME_CHANGE:
			return "UNCERTAIN_NAME_CHANGE";
		default:
			return "NOT_NAME_CHANGE";
		}
	}
	public static int getNodesWithSameBindingInOtherICompilationUnit(String binding, IJavaProject project, ICompilationUnit iunit) throws Exception
	{	
		
		if(!binding.equals(""))
		{	
			ArrayList<ICompilationUnit> siblings = ASTreeManipulationMethods.getSiblingsOfACompilationUnitInItsProject(iunit, project);
			ArrayList<SimpleName> names = new ArrayList<SimpleName>();
		
			for(ICompilationUnit sib: siblings)
			{
				CompilationUnit sibunit = ASTreeManipulationMethods.parseICompilationUnit(sib);
				ArrayList<SimpleName> namesInSib = new SimpleNamesInCompilationUnit(sibunit).getSimpleNamesOfBindingInCompilatioUnit(binding);
				if(namesInSib != null)
					names.addAll(namesInSib);
			}
			return names.size();
		}
		else 
			return 0;
	}
	
	public static int DecideNameChangeType(ASTNode rootOne, ASTNode rootTwo)
	{
		int nameChangeType;
		if(isReferencedVraibleNameChange(rootOne, rootTwo))
			nameChangeType =VARIABLE_NAME_CHANGE_REFERENCE;
		else if(NameChange.isDeclaredVariableNameChange(rootOne, rootTwo))
			nameChangeType = VARIABLE_NAME_CHANGE_DECLARATION;
		else if(NameChange.isInvokedMethodNameChange(rootOne, rootTwo))
			nameChangeType = METHOD_NAME_CHANGE_INVOCATION;
		else if(NameChange.isDeclaredMethodNameChange(rootOne, rootTwo))
			nameChangeType = METHOD_NAME_CHANGE_DECLARATION;
		else if (NameChange.isReferencedTypeNameChange(rootOne, rootTwo))
			nameChangeType = TYPE_NAME_CHANGE_REFERENCE; 
		else if(NameChange.isDeclaredTypeNameChange(rootOne, rootTwo))
			nameChangeType = TYPE_NAME_CHANGE_DECLARATION;
		else if (NameChange.isReferencedPackageNameChange(rootOne, rootTwo))
			nameChangeType = PACKAGE_NAME_CHANGE_REFERENCE;
		else if (NameChange.isDeclaredPackageNameChange(rootOne, rootTwo))
			nameChangeType = PACKAGE_NAME_CHANGE_DECLARATION;
		else if(NameChange.isUncertainNameChange(rootOne, rootTwo))
			nameChangeType = UNCERTAIN_NAME_CHANGE;
		else
			nameChangeType = NameChange.NOT_NAME_CHANGE;	
		return nameChangeType;
	}
	public static boolean isUncertainNameChange(ASTNode rootOne, ASTNode rootTwo)
	{
		return rootOne instanceof SimpleName && rootTwo instanceof SimpleName;
	}
	
	public static boolean isReferencedTypeNameChange(ASTNode rootOne, ASTNode rootTwo)
	{
		if(rootOne instanceof SimpleName && rootTwo instanceof SimpleName)
		{
			SimpleName name1 = (SimpleName)rootOne;
			IBinding bind1 = name1.resolveBinding();
			if(bind1 == null)
				return false;
			if(bind1.getKind() == IBinding.TYPE && !name1.isDeclaration())
				return true;
		}
		return false;
	}
	public static boolean isDeclaredTypeNameChange(ASTNode rootOne, ASTNode rootTwo)
	{
		if(rootOne instanceof SimpleName && rootTwo instanceof SimpleName)
		{
			SimpleName name1 = (SimpleName)rootOne;
			IBinding bind1 = name1.resolveBinding();
			if(bind1 == null)
				return false;
			if(bind1.getKind() == IBinding.TYPE && name1.isDeclaration())
				return true;
		}
		return false;
	}
	public static boolean isReferencedPackageNameChange(ASTNode rootOne, ASTNode rootTwo)
	{
		if(rootOne instanceof SimpleName && rootTwo instanceof SimpleName)
		{
			SimpleName name1 = (SimpleName)rootOne;
			IBinding bind1 = name1.resolveBinding();
			if(bind1 == null)
				return false;
			if(bind1.getKind() == IBinding.PACKAGE && !name1.isDeclaration())
				return true;
		}
		return false;
	}
	public static boolean isDeclaredPackageNameChange(ASTNode rootOne, ASTNode rootTwo)
	{
		if(rootOne instanceof SimpleName && rootTwo instanceof SimpleName)
		{
			SimpleName name1 = (SimpleName)rootOne;
			IBinding bind1 = name1.resolveBinding();
			if(bind1 == null)
				return false;
			if(bind1.getKind() == IBinding.PACKAGE && name1.isDeclaration())
				return true;
		}
		return false;
	}
	
	public static boolean isReferencedVraibleNameChange(ASTNode rootOne, ASTNode rootTwo)
	{
		if(rootOne instanceof SimpleName && rootTwo instanceof SimpleName)
		{
			SimpleName name1 = (SimpleName)rootOne;
			IBinding bind1 = name1.resolveBinding();
			if(bind1 == null)
				return false;
			if(bind1.getKind() == IBinding.VARIABLE && !name1.isDeclaration())
				return true;
		}
		return false;
	}
	public static boolean isDeclaredVariableNameChange(ASTNode rootOne, ASTNode rootTwo)
	{
		if(rootOne instanceof SimpleName && rootTwo instanceof SimpleName)
		{
			SimpleName name1 = (SimpleName)rootOne;
			IBinding bind1 = name1.resolveBinding();
			if(bind1 == null)
				return false;
			if(bind1.getKind() == IBinding.VARIABLE && name1.isDeclaration())
				return true;
		}
		return false;
	}
	
	public static boolean isDeclaredMethodNameChange(ASTNode rootOne, ASTNode rootTwo)
	{
		if(rootOne instanceof SimpleName && rootTwo instanceof SimpleName)
		{
			SimpleName name1 = (SimpleName)rootOne;
			IBinding bind1 = name1.resolveBinding();
			if(bind1 == null)
				return false;
			if(bind1.getKind() == IBinding.METHOD && name1.isDeclaration())
				return true;
		}
		return false;
	}
	
	public static boolean isInvokedMethodNameChange(ASTNode rootOne, ASTNode rootTwo)
	{
		if(rootOne instanceof SimpleName && rootTwo instanceof SimpleName)
		{
			SimpleName name1 = (SimpleName)rootOne;
			IBinding bind1 = name1.resolveBinding();
			if(bind1 == null)
				return false;
			if(bind1.getKind() == IBinding.METHOD && !name1.isDeclaration())
				return true;
		}
		return false;
	}
	public static String[] getOriginalNameAndNewName(ASTNode rootOne, ASTNode rootTwo)
	{
		String[] names =  new String[2];
		if(DecideNameChangeType(rootOne, rootTwo)==NOT_NAME_CHANGE)
			return names;		
		names[0] =((SimpleName)rootOne).getIdentifier();
		names[1] = ((SimpleName)rootTwo).getIdentifier();
		return names;
	}
	
}
