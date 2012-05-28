package extract.localvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;

import userinterface.RefactoringMarker;
import util.UIUtil;

import ASTree.ASTChange;
import ASTree.CUHistory.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.extract.localvariable.JavaRefactoringELVCopy;

public class ELVCopy implements ELVActivity{
	
	CompilationUnitHistoryRecord record;
	
	ELVCopy(CompilationUnitHistoryRecord r)
	{
		record = r;
	}
	
	static private ELVCopy instance;
	public static ELVCopy getNewInstance(CompilationUnitHistoryRecord r)
	{
		 instance = new  ELVCopy(r);
		 return instance;
	}
	public static ELVCopy getCurrentInstance()
	{
		return instance;
	}
	
	public JavaRefactoring getELVRefactoring(ICompilationUnit u) throws Exception {
		JavaEditor editor = UIUtil.getJavaEditor(u);
		int start = record.getHighlightedRegion()[0];
		int line = UIUtil.getLineNumberByOffset(start, editor);
		IMarker marker = RefactoringMarker.addRefactoringMarkerIfNo(u, line);
		return new JavaRefactoringELVCopy(u, line, this);
	}



	public ELVActivity createInstance(CompilationUnitHistoryRecord r) {
		return new ELVCopy(r);
	}



	public CompilationUnitHistoryRecord getRecord() {
		return record;
	}


}




