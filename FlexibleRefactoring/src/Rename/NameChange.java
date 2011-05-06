package Rename;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.*;
public class NameChange {
	
	public static final int NOT_NAME_CHANGE = -1;
	public static final int VARIABLE_NAME_CHANGE_REFERENCE=0;
	public static final int VARIABLE_NAME_CHANGE_DECLARATION = 1;
	public static final int METHOD_NAME_CHANGE_INVOCATION=2;
	public static final int METHOD_NAME_CHANGE_DECLARATION=3;
	
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
		default:
			return "NOT_NAME_CHANGE";
		}
	}
	public static ArrayList<SimpleName> getNodesWithSameBinding(ASTNode node)
	{
		if(node instanceof SimpleName)
		{
			VariableNames names = new VariableNames((CompilationUnit)node.getRoot());
			return names.getTheVariablesOfBinding(((SimpleName)node).resolveBinding());
		}
		else 
			return null;
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
		else
			nameChangeType = NameChange.NOT_NAME_CHANGE;	
		return nameChangeType;
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
