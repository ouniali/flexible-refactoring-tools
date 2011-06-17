package ASTree;

import org.eclipse.text.edits.*;
import org.eclipse.core.runtime.NullProgressMonitor;
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
        	int len = unit.getSourceRange().getLength();
    		ReplaceEdit edit = new ReplaceEdit(0, len,code);   	
			unit.applyTextEdit(edit, monitor);
			commitChangesForICompilationUnit(unit);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	static public void FormattICompilationUnit(ICompilationUnit unit)
	{		
		try {
			NullProgressMonitor monitor = new NullProgressMonitor();
			CodeFormatter formatter = ToolFactory.createCodeFormatter(null);
			TextEdit formatEdit;	
			formatEdit = formatter.format(CodeFormatter.K_COMPILATION_UNIT, unit.getSource(), 0, unit.getSource().length(), 0, unit.findRecommendedLineSeparator());
			unit.applyTextEdit(formatEdit, monitor);
			commitChangesForICompilationUnit(unit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static public void commitChangesForICompilationUnit(ICompilationUnit unit)
	{
		NullProgressMonitor monitor = new NullProgressMonitor();
		try {
			//unit.save(monitor, true);
			unit.makeConsistent(monitor);
			unit.commitWorkingCopy(true, monitor);
			unit.reconcile(AST.JLS3, true, null, monitor);
			unit.makeConsistent(monitor);
			
			/*unit.save(monitor, true);
			unit.commitWorkingCopy(true, monitor);
			
			unit.becomeWorkingCopy(monitor);
			unit.reconcile(AST.JLS3, true, null, monitor);
			unit.makeConsistent(monitor);*/
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}
}
