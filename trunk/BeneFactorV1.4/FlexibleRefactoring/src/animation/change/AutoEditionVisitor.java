package animation.change;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditVisitor;

import animation.autoedition.AtomicEdition;
import animation.autoedition.SingleFileEdition;

public class AutoEditionVisitor extends TextEditVisitor{
	
	SingleFileEdition composite = new SingleFileEdition();
	
	public SingleFileEdition getComposite() {
		return composite;
	}
	
	public void setEnvironment(ICompilationUnit u)
	{
		composite.setICompilationUnit(u);
	}
	
	public void	postVisit(TextEdit edit) 
	{
	}
	
	public boolean	visit(DeleteEdit edit) 
	{
		composite.addEdition(new AtomicEdition(edit));
		return true;
	}
	
	 public boolean visit(InsertEdit edit) 
	 {		 
		 composite.addEdition(new AtomicEdition(edit));
		 return true;
	 }

	 public boolean visit(ReplaceEdit edit) 
	 {
		 composite.addEdition(new AtomicEdition(edit));
		 return true;
	 }
	 
}
