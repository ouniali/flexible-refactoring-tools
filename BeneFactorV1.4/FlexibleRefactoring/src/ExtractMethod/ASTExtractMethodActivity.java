package ExtractMethod;

import org.eclipse.jdt.core.ICompilationUnit;

import utitilies.StringUtilities;
import ASTree.ASTreeManipulationMethods;
import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoringExtractMethod;

public class ASTExtractMethodActivity {

	private final CompilationUnitHistoryRecord record;
	public ASTExtractMethodActivity(CompilationUnitHistoryRecord r)
	{
		record = r;
	}
	
	public JavaRefactoringExtractMethod getJavaExtractMethodRefactoring(ICompilationUnit unit)
	{
		return null;
	}
	
	
	
	public static boolean isCopyingStatements(CompilationUnitHistoryRecord record)
	{
		int start = record.getSeletectedRegion()[0];
		int end = record.getSeletectedRegion()[1];
		int length = end - start + 1;
		String statements = StringUtilities.removeWhiteSpace(record.getSourceCode().substring(start, start + length));
		String block = StringUtilities.removeWhiteSpace(ASTreeManipulationMethods.parseStatements(statements).toString());
		block = block.substring(1, block.length() - 1);		
		if(block.equals(statements))
			return true;
		else
			return false;
	}
	
}
