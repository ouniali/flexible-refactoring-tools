package ExtractMethod;

import org.eclipse.jdt.core.dom.ASTNode;

import ASTree.ASTreeManipulationMethods;
import ASTree.CompilationUnitHistoryRecord;
import ASTree.NewRootPair;

public class ExtractMethod {
	
	public static ASTExtractMethodChangeInformation getExtractMethodASTChangeInformation(CompilationUnitHistoryRecord oldRecord,CompilationUnitHistoryRecord newRecord )
	{

		ASTNode ASTOne = oldRecord.getASTree().getRoot();
		ASTNode ASTTwo = newRecord.getASTree().getRoot();
		NewRootPair pair;
		do
		{
			pair = ASTreeManipulationMethods.traverseToDeepestChange(ASTOne, ASTTwo);
			ASTOne = pair.nodeOne;
			ASTTwo = pair.nodeTwo;
		}while(pair.RootsChanged);
				
		return new ASTExtractMethodChangeInformation(oldRecord, ASTOne, newRecord, ASTTwo);	
	}
}
