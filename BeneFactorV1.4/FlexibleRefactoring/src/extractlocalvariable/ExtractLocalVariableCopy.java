package extractlocalvariable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;

import userinterface.RefactoringMarker;
import util.UIUtil;

import ASTree.ASTChangeInformation;
import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.extractlocalvariable.JavaRefactoringELVCopy;

public class ExtractLocalVariableCopy implements ExtractLocalVariableActivity{
	
	CompilationUnitHistoryRecord record;
	
	ExtractLocalVariableCopy(CompilationUnitHistoryRecord r)
	{
		record = r;
	}
	
	static private ExtractLocalVariableCopy instance;
	public static ExtractLocalVariableCopy getNewInstance(CompilationUnitHistoryRecord r)
	{
		 instance = new  ExtractLocalVariableCopy(r);
		 return instance;
	}
	public static ExtractLocalVariableCopy getCurrentInstance()
	{
		return instance;
	}
	
	public JavaRefactoring getELVRefactoring(ICompilationUnit u) throws Exception {
		JavaEditor editor = UIUtil.getJavaEditor(u);
		int start = record.getHighlightedRegion()[0];
		int line = UIUtil.getLineNumberByOffset(start, editor);
		IMarker marker = RefactoringMarker.addRefactoringMarkerIfNo(u, line);
		return new JavaRefactoringELVCopy(u, line, marker, this);
	}



	public ExtractLocalVariableActivity createInstance(CompilationUnitHistoryRecord r) {
		return new ExtractLocalVariableCopy(r);
	}



	public CompilationUnitHistoryRecord getRecord() {
		return record;
	}


}



interface ExtractLocalVariableActivity{
	public JavaRefactoring getELVRefactoring(ICompilationUnit u) throws Exception;
	public CompilationUnitHistoryRecord getRecord();
	
}
