package extractlocalvariable;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;

import ASTree.ASTChangeInformationGenerator;
import ASTree.ASTreeManipulationMethods;
import ASTree.CompilationUnitHistoryRecord;
import ASTree.NewRootPair;

public class ExtractLocalVariableDetector {
	
	
	private boolean isCutExpressionFound(List<CompilationUnitHistoryRecord> records)
	{
		return false;
	}
	
	
	private boolean isCutExpression(CompilationUnitHistoryRecord r1, CompilationUnitHistoryRecord r2)
	{
		NewRootPair pair = ASTChangeInformationGenerator.getTheDeepestChangedNodePair(r1, r2);
		ASTNode node1 = pair.nodeOne;
		ASTNode node2 = pair.nodeTwo;
		
		return false;
	}
	
	private boolean isCopyExpresstionFound(List<CompilationUnitHistoryRecord> records)
	{
		return false;
	}
	
	private boolean isCopyExpression(CompilationUnitHistoryRecord record)
	{
		boolean copy = record.hasCopyCommand();
		int[] range = record.getHighlightedRegion();
		String exp = record.getSourceCode().substring(range[0], range[1] + 1);
		return copy && isExpression(exp);
	}
	
	private boolean isExpression(String s)
	{
		Expression exp = null;
		try{
			exp = ASTreeManipulationMethods.parseExpression(s);
		
		}catch(Exception e)
		{
			return false;
		}
		return (exp != null);
	}
	
	

}
