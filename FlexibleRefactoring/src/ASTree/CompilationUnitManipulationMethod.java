package ASTree;

import org.eclipse.text.edits.*;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.formatter.CodeFormatter;

public class CompilationUnitManipulationMethod {

	
	static public void UpdateICompilationUnit(ICompilationUnit unit, String code)
	{	    
        try {
        	NullProgressMonitor monitor = new NullProgressMonitor();
        	unit.becomeWorkingCopy(monitor);
        	int oldLen = unit.getSourceRange().getLength();  	
        	ReplaceEdit edit = new ReplaceEdit(0, oldLen, code);   	
			unit.applyTextEdit(edit, monitor);	
			unit.commitWorkingCopy(true, monitor);
			unit.discardWorkingCopy();
			unit.makeConsistent(monitor);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	static public void FormattICompilationUnit(ICompilationUnit unit)
	{		
		try {
			NullProgressMonitor monitor = new NullProgressMonitor();
			unit.becomeWorkingCopy(monitor);
			CodeFormatter formatter = ToolFactory.createCodeFormatter(null);
			TextEdit formatEdit;	
			formatEdit = formatter.format(CodeFormatter.K_COMPILATION_UNIT, unit.getSource(), 0, unit.getSource().length(), 0, unit.findRecommendedLineSeparator());
			unit.applyTextEdit(formatEdit, monitor);
			unit.commitWorkingCopy(true, monitor);
			unit.discardWorkingCopy();
			unit.makeConsistent(monitor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
