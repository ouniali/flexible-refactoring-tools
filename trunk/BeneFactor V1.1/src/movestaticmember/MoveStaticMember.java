package movestaticmember;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import ASTree.ASTChangeInformationGenerator;
import ASTree.CompilationUnitHistoryRecord;

public class MoveStaticMember {

	static int MAXIMUM_LOOK_BACK_COUNT_ADD_STATIC_DECLARATION = 5;
	static int MAXIMUM_LOOK_BACK_COUNT_DELETE_STATIC_DECLARATION = 5;
	static ArrayList<ASTChangeInformationAddStaticMember> detectedAddStaticChange = new ArrayList<ASTChangeInformationAddStaticMember>();
	static ArrayList<ASTChangeInformationDeleteStaticMember> detectedDeleteStaticChange = new ArrayList<ASTChangeInformationDeleteStaticMember>();

	public static void clearAddStaticChange()
	{
		detectedAddStaticChange.clear();
	}
	public static void clearDeleteStaticChange()
	{
		detectedDeleteStaticChange.clear();
	}
	
	
	public static ASTChangeInformationAddStaticMember getLatestAddStaticChange()
	{
		if(detectedAddStaticChange.size() > 0)
			return detectedAddStaticChange.get(detectedAddStaticChange.size() -1);
		else 
			return null;
	}
	
	public static ASTChangeInformationDeleteStaticMember getLatestDeleteStaticChange()
	{
		if(detectedDeleteStaticChange.size() > 0)
			return detectedDeleteStaticChange.get(detectedDeleteStaticChange.size()-1);
		else
			return null;
	}
	
	
	public static boolean LookingBcckForDetectingAddStaticDeclarationChange(ArrayList<CompilationUnitHistoryRecord> Records) throws Exception
	{
		if (Records.size() == 0)
			return false;
		CompilationUnitHistoryRecord latestRecord = Records
				.get(Records.size() - 1);

		if (Records.size() <= 1)
			return false;

		int lookBackCount = Math.min(Records.size() - 1,
				MAXIMUM_LOOK_BACK_COUNT_ADD_STATIC_DECLARATION);
		CompilationUnitHistoryRecord oldRecord;

		for (int i = 1; i <= lookBackCount; i++) 
		{
			int index = Records.size() - 1 - i;
			oldRecord = Records.get(index);
			ASTChangeInformationAddStaticMember change = ASTChangeInformationGenerator.getAddStaticMemberASTChangeInformation(oldRecord, latestRecord);
			if (change != null) 
			{
				detectedAddStaticChange.add(change);
				return true;
			}
		}
		return false;
		
	}
	
	public static boolean LookingBackForDetectingDeleteStaticDeclarationChange(ArrayList<CompilationUnitHistoryRecord> Records)
	{
		if (Records.size() == 0)
			return false;
		CompilationUnitHistoryRecord latestRecord = Records
				.get(Records.size() - 1);

		if (Records.size() <= 1)
			return false;

		int lookBackCount = Math.min(Records.size() - 1,
				MAXIMUM_LOOK_BACK_COUNT_DELETE_STATIC_DECLARATION);
		CompilationUnitHistoryRecord oldRecord;

		for (int i = 1; i <= lookBackCount; i++) 
		{
			int index = Records.size() - 1 - i;
			oldRecord = Records.get(index);
			ASTChangeInformationDeleteStaticMember change = ASTChangeInformationGenerator.getDeleteStaticMemberASTChangeInformation(oldRecord, latestRecord);
			if (change != null) 
			{
				detectedDeleteStaticChange.add(change);
				return true;
			}
		}
		return false;
		
	}
	
	public static int getAddedStaticDeclarationIndex(ASTNode node, String memberDeclaration)
	{
		ArrayList<String> staticMembersDeclarations = ASTVisitorCollectingMembers.getStaticFieldDeclarations(node);
		ArrayList<Integer> staticMembersIndices = ASTVisitorCollectingMembers.getStaticFieldDeclarationsIndices(node);
		for(int i = 0; i< staticMembersDeclarations.size(); i++)
		{
			String s = staticMembersDeclarations.get(i);
			if(s.equals(memberDeclaration))
				return staticMembersIndices.get(i);
		}
		return -1;
	}
	
	
	public static String getAddedStaticDeclaration(ASTNode nodeOne, ASTNode nodeTwo)
	{
		if(nodeOne.getNodeType() == ASTNode.TYPE_DECLARATION && nodeTwo.getNodeType() == ASTNode.TYPE_DECLARATION)
		{
			ArrayList<String> staticMembersOne = ASTVisitorCollectingMembers.getStaticFieldDeclarations(nodeOne);
			ArrayList<String> staticMembersTwo = ASTVisitorCollectingMembers.getStaticFieldDeclarations(nodeTwo);
			if(staticMembersOne.size() < staticMembersTwo.size())
			{
				for(String s : staticMembersOne)
				{
					if(staticMembersTwo.contains(s))
						staticMembersTwo.remove(s);
					else
						return null;
				}				
				if(staticMembersTwo.size() == 1)
					return staticMembersTwo.get(0);
			}
			
		}
		return null;
	}
	
	public static boolean isAddStaticMemberChange(ASTNode nodeOne, ASTNode nodeTwo)
	{
		return getAddedStaticDeclaration(nodeOne, nodeTwo)!=null;
	}
	
	public static boolean isDeleteStaticMemberChange(ASTNode nodeOne, ASTNode nodeTwo)
	{
		return isAddStaticMemberChange(nodeTwo, nodeOne);
	}

}
