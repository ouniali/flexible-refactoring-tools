package ExtractMethod;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;

import userinterface.RefactoringMarker;
import util.StringUtil;
import util.UIUtil;
import ASTree.ASTreeManipulationMethods;
import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.extractmethod.JavaRefactoringExtractMethodActivity;
import JavaRefactoringAPI.extractmethod.JavaRefactoringExtractMethodChange;

public class ASTExtractMethodActivity {

	private final CompilationUnitHistoryRecord record;
	
	public CompilationUnitHistoryRecord getRecord() {
		return record;
	}

	public ASTExtractMethodActivity(CompilationUnitHistoryRecord r)
	{
		record = r;
	}
	
	public JavaRefactoringExtractMethodActivity getJavaExtractMethodRefactoring(ICompilationUnit unit) throws Exception
	{
		int line = getMarkerLineNumber(getCopyStart()) + 1;
		IMarker marker = RefactoringMarker.addRefactoringMarkerIfNo(unit, line);
		return new JavaRefactoringExtractMethodActivity(unit, line, marker, this);
	}
	
	public int getCopyStart()
	{
		return record.getSeletectedRegion()[0];
	}
	
	public int getCopyLength()
	{
		return record.getSeletectedRegion()[1] - record.getSeletectedRegion()[0] + 1;
	}
	
	
	private int getMarkerLineNumber(int offset)
	{
		JavaEditor editor = UIUtil.getActiveJavaEditor();
		return UIUtil.getLineNumberByOffset(offset, editor);
	}
	

	public static boolean isCopyingStatements(CompilationUnitHistoryRecord record)
	{
		if(record.hasCopyCommand())
		{
			int start = record.getSeletectedRegion()[0];
			int end = record.getSeletectedRegion()[1];
			int length = end - start + 1;
			String statements = StringUtil.removeWhiteSpace(record.getSourceCode().substring(start, start + length));
			String block = StringUtil.removeWhiteSpace(ASTreeManipulationMethods.parseStatements(statements).toString());
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
