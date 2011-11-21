package ASTree;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;

public class CompilationUnitManipulationMethod {

	
	static public void UpdateICompilationUnit(ICompilationUnit unit, String code, IProgressMonitor pm)
	{	    
		SubMonitor monitor = SubMonitor.convert(pm,"Performing Code Modification",4);
		
        try {
        	unit.becomeWorkingCopy(monitor.newChild(1));
        	int oldLen = unit.getSourceRange().getLength();  	
        	ReplaceEdit edit = new ReplaceEdit(0, oldLen, code);   	
			unit.applyTextEdit(edit, monitor.newChild(1));	
			unit.commitWorkingCopy(true, monitor.newChild(1));
			unit.discardWorkingCopy();
			unit.makeConsistent(monitor.newChild(1));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		monitor.done();
	}
	
	static public void UpdateICompilationUnitWithoutCommit(ICompilationUnit unit, String code, IProgressMonitor pm)
	{
		SubMonitor monitor = SubMonitor.convert(pm,"Performing Code Modification",4);
		
        try {
        	int oldLen = unit.getSourceRange().getLength();  	
        	ReplaceEdit edit = new ReplaceEdit(0, oldLen, code);
			unit.applyTextEdit(edit, monitor.newChild(1));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		monitor.done();
	}
	
	@Deprecated 
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
