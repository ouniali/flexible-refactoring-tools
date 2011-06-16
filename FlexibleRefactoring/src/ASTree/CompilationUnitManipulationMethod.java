package ASTree;

import org.eclipse.text.edits.*;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.formatter.CodeFormatter;

public class CompilationUnitManipulationMethod {

	
	static public void UpdateICompilationUnit(ICompilationUnit unit, String code)
	{	    
        try {
        	int len = unit.getSourceRange().getLength();
    		ReplaceEdit edit = new ReplaceEdit(0, len,code);
        	NullProgressMonitor monitor = new NullProgressMonitor();
			unit.applyTextEdit(edit, monitor);
			unit.makeConsistent(monitor);
			unit.becomeWorkingCopy(monitor);
			unit.save(monitor, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	@SuppressWarnings("restriction")
	static public void FormattICompilationUnit(ICompilationUnit unit)
	{		
		try {
			CodeFormatter formatter = ToolFactory.createCodeFormatter(null);
			TextEdit formatEdit;
			NullProgressMonitor monitor = new NullProgressMonitor();
			formatEdit = formatter.format(CodeFormatter.K_COMPILATION_UNIT, unit.getSource(), 0, unit.getSource().length(), 0, unit.findRecommendedLineSeparator());
			unit.makeConsistent(monitor);
			unit.applyTextEdit(formatEdit, monitor);
			unit.makeConsistent(monitor);
			//unit.save(monitor, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
