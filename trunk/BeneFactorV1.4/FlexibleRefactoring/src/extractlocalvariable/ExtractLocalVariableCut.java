package extractlocalvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;

import userinterface.RefactoringMarker;
import util.UIUtil;

import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.extractlocalvariable.JavaRefactoringELVCopy;
import JavaRefactoringAPI.extractlocalvariable.JavaRefactoringELVCut;

public class ExtractLocalVariableCut implements ExtractLocalVariableActivity{

	CompilationUnitHistoryRecord record;
	
	private ExtractLocalVariableCut(CompilationUnitHistoryRecord r)
	{
		record = r;
	}
	
	static private ExtractLocalVariableCut instance;
	public static ExtractLocalVariableCut getNewInstance(CompilationUnitHistoryRecord r)
	{
		 instance = new  ExtractLocalVariableCut(r);
		 return instance;
	}
	public static ExtractLocalVariableCut getCurrentInstance()
	{
		return instance;
	}
	
	
	
	public CompilationUnitHistoryRecord getRecord() {
		return record;
	}
		
	
	public JavaRefactoring getELVRefactoring(ICompilationUnit u) throws Exception {
		JavaEditor editor = UIUtil.getJavaEditor(u);
		int start = record.getHighlightedRegion()[0];
		int line = UIUtil.getLineNumberByOffset(start, editor);
		IMarker marker = RefactoringMarker.addRefactoringMarkerIfNo(u, line);
		return new JavaRefactoringELVCut(u, line, marker, this);
	}

}
