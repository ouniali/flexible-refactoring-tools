package movestaticmember;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;

public class MoveStaticMember {

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
