package ExtractMethod;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;

import userinterface.RefactoringMarker;
import utitilies.StringUtilities;
import ASTree.ASTreeManipulationMethods;
import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.extractmethod.JavaRefactoringExtractMethodActivity;
import JavaRefactoringAPI.extractmethod.JavaRefactoringExtractMethodChange;

public class ASTExtractMethodActivity {

	private final CompilationUnitHistoryRecord record;
	
	public ASTExtractMethodActivity(CompilationUnitHistoryRecord r)
	{
		record = r;
	}
	
	public JavaRefactoringExtractMethodActivity getJavaExtractMethodRefactoring(ICompilationUnit unit) throws Exception
	{
		int line = 10;
		IMarker marker = RefactoringMarker.addRefactoringMarkerIfNo(unit, line);
		return new JavaRefactoringExtractMethodActivity(unit, line, marker, this);
	}
	
	
	
	public static boolean isCopyingStatements(CompilationUnitHistoryRecord record)
	{
		if(record.hasCopyCommand())
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
		else
			return false;
	}
	
	
	public boolean equals(Object o)
	{
		CompilationUnitHistoryRecord another = ((ASTExtractMethodActivity)o).record;
		return record.equals(another);
	}
}
