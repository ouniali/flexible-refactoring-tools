package ASTree;

import java.util.ArrayList;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import Rename.ASTNameChangeInformation;


public class ASTreeManipulationMethods {
		
	
	public static ASTChangeInformation getGeneralASTChangeInformation(CompilationUnitHistoryRecord oldRecord,CompilationUnitHistoryRecord newRecord )
	{

		ASTNode ASTOne = oldRecord.getASTree().getRoot();
		ASTNode ASTTwo = newRecord.getASTree().getRoot();
		NewRootPair pair;
		do
		{
			pair = traverseToDeepestChange(ASTOne, ASTTwo);
			ASTOne = pair.nodeOne;
			ASTTwo = pair.nodeTwo;
		}while(pair.RootsChanged);
				
		return new ASTChangeInformation(oldRecord, ASTOne, newRecord, ASTTwo);	
	}
	
	public static ASTNameChangeInformation getRenameASTChangedInformation(CompilationUnitHistoryRecord oldRecord,CompilationUnitHistoryRecord newRecord) throws Exception
	{
		ASTNode ASTOne = oldRecord.getASTree().getRoot();
		ASTNode ASTTwo = newRecord.getASTree().getRoot();
		NewRootPair pair;
		do
		{
			pair = traverseToDeepestChange(ASTOne, ASTTwo);
			ASTOne = pair.nodeOne;
			ASTTwo = pair.nodeTwo;
		}while(pair.RootsChanged);
		
		if(ASTOne instanceof Name && ASTTwo instanceof Name)
			return new ASTNameChangeInformation(oldRecord, ASTOne, newRecord, ASTTwo);
			
		return null; 	
	}

	private static NewRootPair traverseToDeepestChange(ASTNode AstOne, ASTNode AstTwo)
	{
		ArrayList<ASTNode> childrenOne = getChildNodes(AstOne);
		ArrayList<ASTNode> childrenTwo = getChildNodes(AstTwo);
			
		if(childrenOne.size() != childrenTwo.size())
		{
			return new NewRootPair(false, AstOne, AstTwo);
		}
		
		int differentSubtreeCount=0;
		ASTNode changedNodeOne = null;
		ASTNode changedNodeTwo = null;
		
		for(int i = 0; i<childrenOne.size(); i++)
		{
			ASTNode node1 = childrenOne.get(i);
			ASTNode node2 = childrenTwo.get(i);
			if(!node1.subtreeMatch(new ASTMatcher(),node2))
			{	
				differentSubtreeCount++;
				changedNodeOne = node1;
				changedNodeTwo = node2;	
			}
		}
		
		if(differentSubtreeCount == 1)
			return new NewRootPair(true, changedNodeOne, changedNodeTwo);
		return new NewRootPair(false, AstOne, AstTwo);
	}
	
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
	
	public static CompilationUnit parseICompilationUnit(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		
		return (CompilationUnit) parser.createAST(null); // parse
	}
	
	public static CompilationUnit parseSourceCode(String code)
	{
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(code.toCharArray());
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
	
}

