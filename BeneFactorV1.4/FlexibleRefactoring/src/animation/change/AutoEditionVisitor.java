package animation.change;

import java.util.ArrayList;

import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditVisitor;

import animation.autoedition.AtomicEdition;
import animation.autoedition.AtomicEditionComposite;

public class AutoEditionVisitor extends TextEditVisitor{
	
	AtomicEditionComposite composite = new AtomicEditionComposite();
	
	public AtomicEditionComposite getComposite() {
		return composite;
	}
	
	public void	postVisit(TextEdit edit) 
	{
		System.out.println(edit);
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
