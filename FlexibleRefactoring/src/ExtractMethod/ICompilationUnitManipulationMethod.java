package ExtractMethod;

import org.eclipse.text.edits.*;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;

public class ICompilationUnitManipulationMethod {

	
	static public void UpdateICompilationUnit(ICompilationUnit unit, String code)
	{	    
        try {
        	int len = unit.getSourceRange().getLength();
    		ReplaceEdit edit = new ReplaceEdit(0, len,code);
        	NullProgressMonitor monitor = new NullProgressMonitor();
			unit.applyTextEdit(edit, monitor);
			unit.makeConsistent(monitor);
			unit.save(monitor, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
}
