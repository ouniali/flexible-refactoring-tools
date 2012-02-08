package animation;

import org.eclipse.text.edits.*;

public class RefactoringTextEditorVisitor extends TextEditVisitor{
	
	public void	postVisit(TextEdit edit) 
	{
		System.out.println(edit);
	}
	public boolean	visit(DeleteEdit edit) 
	{
		
		return true;
	}
	
	 public boolean visit(InsertEdit edit) 
	 {
		 
		 return true;
	 }

	 public boolean visit(ReplaceEdit edit) 
	 {
		 String replacement = edit.getText();
		 int start = edit.getOffset();
		 int end = edit.getInclusiveEnd();
		 return true;
	 }
}
