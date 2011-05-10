package ASTree;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.*;

import Rename.ASTNameChangeInformation;


public class ASTree {
		
	public static void OutputTree(CompilationUnit Root)
	{
		Root.accept(new OutputASTVisitor());
	}
	
	public static ASTChangeInformation getGeneralASTChangeInformation(CompilationUnit AstOld, long oldTime, CompilationUnit AstNew, long newTime)
	{
		ASTNode ASTOne = AstOld.getRoot();
		ASTNode ASTTwo = AstNew.getRoot();
		NewRootPair pair;
		do
		{
			pair = traverseToDeepestChange(ASTOne, ASTTwo);
			ASTOne = pair.nodeOne;
			ASTTwo = pair.nodeTwo;
		}while(pair.RootsChanged);
				
		return new ASTChangeInformation(ASTOne, oldTime, ASTTwo, newTime);	
	}
	
	public static ASTNameChangeInformation getRenameASTChangedInformation(CompilationUnit AstOld, long oldTime, CompilationUnit AstNew, long newTime)
	{
		ASTNode ASTOne = AstOld.getRoot();
		ASTNode ASTTwo = AstNew.getRoot();
		NewRootPair pair;
		do
		{
			pair = traverseToDeepestChange(ASTOne, ASTTwo);
			ASTOne = pair.nodeOne;
			ASTTwo = pair.nodeTwo;
		}while(pair.RootsChanged);
				
		return new ASTNameChangeInformation(ASTOne, oldTime, ASTTwo, newTime);	
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
	public static CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
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
	
}


