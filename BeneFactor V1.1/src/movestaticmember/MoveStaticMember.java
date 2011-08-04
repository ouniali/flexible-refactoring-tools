package movestaticmember;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;

public class MoveStaticMember {

	public static boolean isAddStaticMemberChange(ASTNode nodeOne, ASTNode nodeTwo)
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
						return false;
				}				
				return staticMembersTwo.size() > 0;
			}
			
		}
		return false;
	}
	
	public static boolean isDeleteStaticMemberChange(ASTNode nodeOne, ASTNode nodeTwo)
	{
		return isAddStaticMemberChange(nodeTwo, nodeOne);
	}

}
