package animation;

import java.util.ArrayList;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.text.edits.*;

import utitilies.UserInterfaceUtilities;

public class RefactoringTextEditorVisitor extends TextEditVisitor{
	
	Animation anim = new Animation();
	
	public Animation getAnimation()
	{
		return anim;
	}
	
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
		 System.out.println(start);
		 MovableText mt = MovableText.MovableTextFactory(1, 1, replacement);
		 JavaEditor editor = UserInterfaceUtilities.getActiveJavaEditor();
		 Point p = UserInterfaceUtilities.getEditorPointInDisplay(start, editor);
		 mt.setDestination(p);
		 anim.addMovableObject(mt);
		 return true;
	 }
}
