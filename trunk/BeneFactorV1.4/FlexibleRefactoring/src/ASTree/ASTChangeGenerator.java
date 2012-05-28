package ASTree;

import java.util.ArrayList;

import movestaticmember.ASTChangeAddStaticMember;
import movestaticmember.ASTChangeDeleteStaticMember;
import movestaticmember.MoveStaticMember;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;

import extract.method.ASTChangeEM;
import extract.method.EMDetector;
import extract.method.EMUtil;

import util.ASTUtil;

import ASTree.CUHistory.CompilationUnitHistoryRecord;
import Rename.ASTChangeName;
import Rename.NameChangeUtil;

public class ASTChangeGenerator {

	public static ASTChange getGeneralASTChangeInformation(CompilationUnitHistoryRecord oldRecord,CompilationUnitHistoryRecord newRecord )
	{
		NewRootPair pair = getDeepestChangedNodePair(oldRecord, newRecord);	
		return new ASTChange(oldRecord, pair.nodeOne, newRecord, pair.nodeTwo);	
	}

	public static ASTChangeName getRenameASTChangedInformation(CompilationUnitHistoryRecord oldRecord,CompilationUnitHistoryRecord newRecord) throws Exception
	{
		NewRootPair pair = getDeepestChangedNodePair(oldRecord, newRecord);
		if(NameChangeUtil.isRenameChange(pair.nodeOne, pair.nodeTwo))
			return new ASTChangeName(oldRecord, pair.nodeOne, newRecord, pair.nodeTwo);		
		else
			return null; 	
	}
	
	public static ASTChangeEM getExtractMethodASTChangeInformation(CompilationUnitHistoryRecord oldRecord,CompilationUnitHistoryRecord newRecord )
	{
		NewRootPair pair = getDeepestChangedNodePair(oldRecord, newRecord);		
		if(EMUtil.isExtractMethodChange(oldRecord, pair.nodeOne, newRecord, pair.nodeTwo))
			return new ASTChangeEM(oldRecord, pair.nodeOne, newRecord, pair.nodeTwo);	
		else
			return null;
	}
	
	public static ASTChangeAddStaticMember getAddStaticMemberASTChangeInformation(CompilationUnitHistoryRecord oldRecord, CompilationUnitHistoryRecord newRecord)
	{
		NewRootPair pair = getDeepestChangedNodePair(oldRecord, newRecord);
		if(MoveStaticMember.isAddStaticMemberChange(pair.nodeOne, pair.nodeTwo))
			return new ASTChangeAddStaticMember(oldRecord, pair.nodeOne, newRecord, pair.nodeTwo);
		else 
			return null;
	}
	
	public static ASTChangeDeleteStaticMember getDeleteStaticMemberASTChangeInformation(CompilationUnitHistoryRecord oldRecord, CompilationUnitHistoryRecord newRecord)
	{
		NewRootPair pair = getDeepestChangedNodePair(oldRecord, newRecord);
		if(MoveStaticMember.isDeleteStaticMemberChange(pair.nodeOne, pair.nodeTwo))
			return new ASTChangeDeleteStaticMember(oldRecord, pair.nodeOne, newRecord, pair.nodeTwo);
		else 
			return null;
	}	
	
	public static NewRootPair getDeepestChangedNodePair(CompilationUnitHistoryRecord oldRecord,CompilationUnitHistoryRecord newRecord)
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
		return pair;
	}
	
	public static NewRootPair traverseToDeepestChange(ASTNode AstOne, ASTNode AstTwo)
	{
		ArrayList<ASTNode> childrenOne = ASTUtil.getChildNodes(AstOne);
		ArrayList<ASTNode> childrenTwo = ASTUtil.getChildNodes(AstTwo);
			
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
	
}
