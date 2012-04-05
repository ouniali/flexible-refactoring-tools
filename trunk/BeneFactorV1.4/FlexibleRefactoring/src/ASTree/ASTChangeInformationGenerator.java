package ASTree;

import java.util.ArrayList;

import movestaticmember.ASTChangeInformationAddStaticMember;
import movestaticmember.ASTChangeInformationDeleteStaticMember;
import movestaticmember.MoveStaticMember;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;

import ExtractMethod.ASTExtractMethodChangeInformation;
import ExtractMethod.ExtractMethod;
import Rename.ASTNameChangeInformation;
import Rename.NameChange;

public class ASTChangeInformationGenerator {

	public static ASTChangeInformation getGeneralASTChangeInformation(CompilationUnitHistoryRecord oldRecord,CompilationUnitHistoryRecord newRecord )
	{
		NewRootPair pair = getTheDeepestChangedNodePair(oldRecord, newRecord);	
		return new ASTChangeInformation(oldRecord, pair.nodeOne, newRecord, pair.nodeTwo);	
	}

	public static ASTNameChangeInformation getRenameASTChangedInformation(CompilationUnitHistoryRecord oldRecord,CompilationUnitHistoryRecord newRecord) throws Exception
	{
		NewRootPair pair = getTheDeepestChangedNodePair(oldRecord, newRecord);
		//System.out.println(pair);
		if(NameChange.isRenameChange(pair.nodeOne, pair.nodeTwo))
			return new ASTNameChangeInformation(oldRecord, pair.nodeOne, newRecord, pair.nodeTwo);		
		else
			return null; 	
	}
	
	public static ASTExtractMethodChangeInformation getExtractMethodASTChangeInformation(CompilationUnitHistoryRecord oldRecord,CompilationUnitHistoryRecord newRecord )
	{
		NewRootPair pair = getTheDeepestChangedNodePair(oldRecord, newRecord);		
		if(ExtractMethod.isExtractMethodChange(oldRecord, pair.nodeOne, newRecord, pair.nodeTwo))
			return new ASTExtractMethodChangeInformation(oldRecord, pair.nodeOne, newRecord, pair.nodeTwo);	
		else
			return null;
	}
	
	public static ASTChangeInformationAddStaticMember getAddStaticMemberASTChangeInformation(CompilationUnitHistoryRecord oldRecord, CompilationUnitHistoryRecord newRecord)
	{
		NewRootPair pair = getTheDeepestChangedNodePair(oldRecord, newRecord);
		if(MoveStaticMember.isAddStaticMemberChange(pair.nodeOne, pair.nodeTwo))
			return new ASTChangeInformationAddStaticMember(oldRecord, pair.nodeOne, newRecord, pair.nodeTwo);
		else 
			return null;
	}
	
	public static ASTChangeInformationDeleteStaticMember getDeleteStaticMemberASTChangeInformation(CompilationUnitHistoryRecord oldRecord, CompilationUnitHistoryRecord newRecord)
	{
		NewRootPair pair = getTheDeepestChangedNodePair(oldRecord, newRecord);
		if(MoveStaticMember.isDeleteStaticMemberChange(pair.nodeOne, pair.nodeTwo))
			return new ASTChangeInformationDeleteStaticMember(oldRecord, pair.nodeOne, newRecord, pair.nodeTwo);
		else 
			return null;
	}
	
	
	public static NewRootPair getTheDeepestChangedNodePair(CompilationUnitHistoryRecord oldRecord,CompilationUnitHistoryRecord newRecord)
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
		ArrayList<ASTNode> childrenOne = ASTreeManipulationMethods.getChildNodes(AstOne);
		ArrayList<ASTNode> childrenTwo = ASTreeManipulationMethods.getChildNodes(AstTwo);
			
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
