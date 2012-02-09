package animation;

import java.util.ArrayList;

import org.eclipse.text.edits.*;

public class RefactoringTextEditorVisitor extends TextEditVisitor{
	
	ArrayList<MovableObject> m_objects = new ArrayList<MovableObject>();
	
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
		 MovableText mt = MovableText.MovableTextFactory(0, 0, replacement);
		 mt.setDestination();
		 m_objects.add(mc);
		 return true;
	 }
}
