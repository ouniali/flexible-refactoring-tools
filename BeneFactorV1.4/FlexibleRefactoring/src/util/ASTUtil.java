package util;

import java.util.ArrayList;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;
import ASTree.visitors.ASTNodeIndexVisitor;
import ASTree.visitors.GetChildrenNodeVisitor;



public class ASTUtil {
		
	public static ArrayList<ASTNode> getRemainingNodes(ArrayList<ASTNode> totalNodes, ArrayList<ASTNode> deletedNodes)
	{
		ArrayList<ASTNode> remainingNodes = new ArrayList<ASTNode>();
		for(ASTNode node: totalNodes)
		{
			if(!deletedNodes.contains(node))
			{
				remainingNodes.add(node);
			}
		}
		
		return remainingNodes;
	}

	public static ArrayList<ICompilationUnit> getSiblingsOfACompilationUnitInItsProject(ICompilationUnit iunit, IJavaProject project) throws Exception
	{
		ArrayList<ICompilationUnit> siblings = new ArrayList<ICompilationUnit>();
		ArrayList<ICompilationUnit> allUnits = getICompilationUnitsOfAProject(project);
		
		for(ICompilationUnit item: allUnits)
		{
			if(!item.getElementName().equals(iunit.getElementName()))
				siblings.add(item);
		}
			
		return siblings;
	}
	
	public static ArrayList<ICompilationUnit> getICompilationUnitsOfAProject(IJavaProject project) throws Exception
	{
		ArrayList<ICompilationUnit> result = new ArrayList<ICompilationUnit>();
		IPackageFragment[] packages = project.getPackageFragments();
		for (IPackageFragment mypackage : packages) 
		{
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) 
			{
				for (ICompilationUnit unit : mypackage.getCompilationUnits())
					result.add(unit);
			}	
		}
		return result;
	}
	
	public static IPackageFragment getContainingPackage(ICompilationUnit unit)
	{
		IJavaElement element = unit.getParent();
		if(element.getElementType() == IType.PACKAGE_FRAGMENT)
			return (IPackageFragment)element;
		else
			return null;
	}
	
	public static ArrayList<ICompilationUnit> getICompilationUnitOfPackage(IPackageFragment p) throws JavaModelException
	{
		Assert.isTrue(p.getKind() == IPackageFragmentRoot.K_SOURCE);
		ArrayList<ICompilationUnit> result = new ArrayList<ICompilationUnit>();
		for (ICompilationUnit unit : p.getCompilationUnits())
			result.add(unit);
		return result;
	}
	
	public static CompilationUnit parseICompilationUnit(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setIgnoreMethodBodies(false);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}
	
	public static Block parseStatements(String source)
	{
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setIgnoreMethodBodies(false);
		parser.setKind(ASTParser.K_STATEMENTS);
		parser.setSource(source.toCharArray());
		parser.setResolveBindings(false);
		return (Block) parser.createAST(null);
	}
	
	public static Expression parseExpression(String source)
	{
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_EXPRESSION);
		return (Expression) parser.createAST(null);
	}
	
	public static boolean isExpression(String s)
	{
		Expression exp = null;
		try{
			exp = ASTUtil.parseExpression(s);
		}catch(Exception e)
		{
			return false;
		}
		return (exp != null);
	}
	
	public static CompilationUnit parseSourceCode(String code)
	{
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setIgnoreMethodBodies(false);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(code.toCharArray());
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setResolveBindings(true);
		CompilationUnit unit = (CompilationUnit) parser.createAST(null); 
		return unit;
	}

	
	static public String getCompilationUnitName(CompilationUnit unit)
	{	
		return unit.getTypeRoot().getElementName().replace(".java", "");
	}
	
	static public String getPackageName(PackageDeclaration pac)
	{
		return pac.getName().toString();
	}
	
	public static ArrayList<ASTNode> getChildNodes(ASTNode parent)
	{
		GetChildrenNodeVisitor visitor = new GetChildrenNodeVisitor(parent);
		parent.accept(visitor);
		return visitor.getChildrenList();
	}
	
	public static int getRankingInChildren(ASTNode parent, ASTNode son)
	{
		ArrayList<ASTNode> siblings = getChildNodes(parent);
		return siblings.indexOf(son);
	}
	
	public static String getJavaProjectName(IJavaProject project)
	{
		return project.getElementName();
	}
	
	public static int getASTNodeIndexInCompilationUnit(ASTNode node)
	{
		CompilationUnit unit = (CompilationUnit)node.getRoot();
		ASTNodeIndexVisitor visitor = new ASTNodeIndexVisitor(node);
		unit.accept(visitor);	
		return visitor.getTargetNodeIndex();
	}
	
	public static ASTNode getASTNodeByIndex(CompilationUnit unit, int index)
	{
		ASTNodeIndexVisitor visitor = new ASTNodeIndexVisitor(index);
		unit.accept(visitor);
		return visitor.getTargetNode();
	}
	
	public static ArrayList<ASTNode> getOffsprings (ASTNode node)
	{
		ArrayList<ASTNode> offsprings = new ArrayList<ASTNode>();
		offsprings.add(node);
		for(int i=0;;i++)
		{
			if(i == offsprings.size())
				break;
			ASTNode current = offsprings.get(i);
			offsprings.addAll(getChildNodes(current));
		}
		return offsprings;
	}
	
}


