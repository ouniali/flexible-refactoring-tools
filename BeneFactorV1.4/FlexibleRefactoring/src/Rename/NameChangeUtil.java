package Rename;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.*;

import util.ASTUtil;

import ASTree.ASTChangeGenerator;
import ASTree.CUHistory.CompilationUnitHistoryRecord;

public class NameChangeUtil {

	public static final int NOT_NAME_CHANGE = -1;

	public static final int VARIABLE_NAME_CHANGE_REFERENCE = 0;
	public static final int VARIABLE_NAME_CHANGE_DECLARATION = 1;
	public static final int METHOD_NAME_CHANGE_INVOCATION = 2;
	public static final int METHOD_NAME_CHANGE_DECLARATION = 3;
	public static final int TYPE_NAME_CHANGE_REFERENCE = 4;
	public static final int TYPE_NAME_CHANGE_DECLARATION = 5;
	public static final int PACKAGE_NAME_CHANGE_REFERENCE = 6;
	public static final int PACKAGE_NAME_CHANGE_DECLARATION = 7;

	public static final int UNCERTAIN_NAME_CHANGE = 8;


	
	
	public static boolean isRenameChange(ASTNode node1, ASTNode node2) {
		if (node1 instanceof Name && node2 instanceof Name)
			return true;
		else
			return false;
	}

	public static boolean isNameInDeclaration(Name name) {
		if (name instanceof SimpleName) {
			SimpleName sName = (SimpleName) name;
			return sName.isDeclaration();
		} else
			return false;
	}



	public static String getNameChangeTypeDescription(int nameChangeType) {
		switch (nameChangeType) {
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

	public static int getNodesWithSameBindingInOtherICompilationUnit(
			String binding, IJavaProject project, ICompilationUnit iunit)
			throws Exception {

		if (!binding.equals("")) {
			ArrayList<ICompilationUnit> siblings = ASTUtil
					.getSiblingsOfACompilationUnitInItsProject(iunit, project);
			ArrayList<Integer> names = new ArrayList<Integer>();

			for (ICompilationUnit sib : siblings) {
				ArrayList<Integer> namesInSib = new NamesInCompilationUnit(sib)
						.getNameIndicesOfBindingInCompilatioUnit(binding);
				if (namesInSib != null)
					names.addAll(namesInSib);
			}
			return names.size();
		} else
			return 0;
	}

	public static int DecideNameChangeType(ASTNode rootOne, ASTNode rootTwo) {
		int nameChangeType;
		if (NameChangeUtil.isReferencedVraibleNameChange(rootOne, rootTwo))
			nameChangeType = VARIABLE_NAME_CHANGE_REFERENCE;
		else if (NameChangeUtil.isDeclaredVariableNameChange(rootOne, rootTwo))
			nameChangeType = VARIABLE_NAME_CHANGE_DECLARATION;
		else if (NameChangeUtil.isInvokedMethodNameChange(rootOne, rootTwo))
			nameChangeType = METHOD_NAME_CHANGE_INVOCATION;
		else if (NameChangeUtil.isDeclaredMethodNameChange(rootOne, rootTwo))
			nameChangeType = METHOD_NAME_CHANGE_DECLARATION;
		else if (NameChangeUtil.isReferencedTypeNameChange(rootOne, rootTwo))
			nameChangeType = TYPE_NAME_CHANGE_REFERENCE;
		else if (NameChangeUtil.isDeclaredTypeNameChange(rootOne, rootTwo))
			nameChangeType = TYPE_NAME_CHANGE_DECLARATION;
		else if (NameChangeUtil.isReferencedPackageNameChange(rootOne, rootTwo))
			nameChangeType = PACKAGE_NAME_CHANGE_REFERENCE;
		else if (NameChangeUtil.isDeclaredPackageNameChange(rootOne, rootTwo))
			nameChangeType = PACKAGE_NAME_CHANGE_DECLARATION;
		else if (NameChangeUtil.isUncertainNameChange(rootOne, rootTwo))
			nameChangeType = UNCERTAIN_NAME_CHANGE;
		else
			nameChangeType = NameChangeUtil.NOT_NAME_CHANGE;
		return nameChangeType;
	}

	public static boolean isUncertainNameChange(ASTNode rootOne, ASTNode rootTwo) {
		return rootOne instanceof Name && rootTwo instanceof Name;
	}

	public static boolean isReferencedTypeNameChange(ASTNode rootOne,
			ASTNode rootTwo) {
		if (rootOne instanceof Name && rootTwo instanceof Name) {
			Name name1 = (Name) rootOne;
			IBinding bind1 = name1.resolveBinding();
			if (bind1 == null)
				return false;
			if (bind1.getKind() == IBinding.TYPE && isNameInDeclaration(name1))
				return true;
		}
		return false;
	}

	public static boolean isDeclaredTypeNameChange(ASTNode rootOne,
			ASTNode rootTwo) {
		if (rootOne instanceof SimpleName && rootTwo instanceof SimpleName) {
			SimpleName name1 = (SimpleName) rootOne;
			IBinding bind1 = name1.resolveBinding();
			if (bind1 == null)
				return false;
			if (bind1.getKind() == IBinding.TYPE && name1.isDeclaration())
				return true;
		}
		return false;
	}

	public static boolean isReferencedPackageNameChange(ASTNode rootOne,
			ASTNode rootTwo) {
		if (rootOne instanceof SimpleName && rootTwo instanceof SimpleName) {
			SimpleName name1 = (SimpleName) rootOne;
			IBinding bind1 = name1.resolveBinding();
			if (bind1 == null)
				return false;
			if (bind1.getKind() == IBinding.PACKAGE && !name1.isDeclaration())
				return true;
		}
		return false;
	}

	public static boolean isDeclaredPackageNameChange(ASTNode rootOne,
			ASTNode rootTwo) {
		if (rootOne instanceof SimpleName && rootTwo instanceof SimpleName) {
			SimpleName name1 = (SimpleName) rootOne;
			IBinding bind1 = name1.resolveBinding();
			if (bind1 == null)
				return false;
			if (bind1.getKind() == IBinding.PACKAGE && name1.isDeclaration())
				return true;
		}
		return false;
	}

	public static boolean isReferencedVraibleNameChange(ASTNode rootOne,
			ASTNode rootTwo) {
		if (rootOne instanceof SimpleName && rootTwo instanceof SimpleName) {
			SimpleName name1 = (SimpleName) rootOne;
			IBinding bind1 = name1.resolveBinding();
			if (bind1 == null)
				return false;
			if (bind1.getKind() == IBinding.VARIABLE && !name1.isDeclaration())
				return true;
		}
		return false;
	}

	public static boolean isDeclaredVariableNameChange(ASTNode rootOne,
			ASTNode rootTwo) {
		if (rootOne instanceof SimpleName && rootTwo instanceof SimpleName) {
			SimpleName name1 = (SimpleName) rootOne;
			IBinding bind1 = name1.resolveBinding();
			if (bind1 == null)
				return false;
			if (bind1.getKind() == IBinding.VARIABLE && name1.isDeclaration())
				return true;
		}
		return false;
	}

	public static boolean isDeclaredMethodNameChange(ASTNode rootOne,
			ASTNode rootTwo) {
		if (rootOne instanceof SimpleName && rootTwo instanceof SimpleName) {
			SimpleName name1 = (SimpleName) rootOne;
			IBinding bind1 = name1.resolveBinding();
			if (bind1 == null)
				return false;
			if (bind1.getKind() == IBinding.METHOD && name1.isDeclaration())
				return true;
		}
		return false;
	}

	public static boolean isInvokedMethodNameChange(ASTNode rootOne,
			ASTNode rootTwo) {
		if (rootOne instanceof SimpleName && rootTwo instanceof SimpleName) {
			SimpleName name1 = (SimpleName) rootOne;
			IBinding bind1 = name1.resolveBinding();
			if (bind1 == null)
				return false;
			if (bind1.getKind() == IBinding.METHOD && !name1.isDeclaration())
				return true;
		}
		return false;
	}

	public static String[] getOriginalNameAndNewName(ASTNode rootOne,
			ASTNode rootTwo) {
		String[] names = new String[2];
		if (DecideNameChangeType(rootOne, rootTwo) == NOT_NAME_CHANGE)
			return names;
		names[0] = ((SimpleName) rootOne).getIdentifier();
		names[1] = ((SimpleName) rootTwo).getIdentifier();
		return names;
	}

}
