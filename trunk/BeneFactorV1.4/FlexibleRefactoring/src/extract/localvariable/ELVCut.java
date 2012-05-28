package extract.localvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;

import userinterface.RefactoringMarker;
import util.UIUtil;

import ASTree.CUHistory.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.extract.localvariable.JavaRefactoringELVCut;

public class ELVCut implements ELVActivity{

	CompilationUnitHistoryRecord record;
	
	private ELVCut(CompilationUnitHistoryRecord r)
	{
		record = r;
	}
	
	static private ELVCut instance;
	public static ELVCut getNewInstance(CompilationUnitHistoryRecord r)
	{
		 instance = new  ELVCut(r);
		 return instance;
	}
	public static ELVCut getCurrentInstance()
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
		return new JavaRefactoringELVCut(u, line, this);
	}

}
