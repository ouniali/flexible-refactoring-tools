package animation;

import java.util.Stack;

import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.NullChange;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.TextEditBasedChange;
import org.eclipse.ltk.core.refactoring.TextEditBasedChangeGroup;
import org.eclipse.ltk.core.refactoring.TextEditChangeGroup;
import org.eclipse.ltk.core.refactoring.UndoTextFileChange;
import org.eclipse.ltk.core.refactoring.resource.ResourceChange;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditVisitor;

public class AnimatedChange {
	
	Change change;
	Animation animation = new Animation();
	
	public AnimatedChange(Change c)
	{
		change = c;
		if(change instanceof CompositeChange)
			VisitCompositeChangeTree((CompositeChange)change);		
	}
	
	public void play()
	{
		animation.play();
	}
	
	private void VisitCompositeChangeTree(CompositeChange cc)
	{
		Stack<Change> stack = new Stack<Change>();
		stack.push(cc);
		
		while(!stack.empty())
		{
			Change c = (Change) stack.pop();
			if(c instanceof CompositeChange)
			{
				CompositeChange com = (CompositeChange) c;
				Change[] children = com.getChildren();
				for(int i = children.length - 1; i >= 0; i--)
					stack.push(children[i]);
			}
			else
				VisitChange(c);
			
		}
		
	}
	
	private void VisitChange(Change c) {
		if(c instanceof TextChange)
			VisitChange((TextChange)c);
		else if(c instanceof NullChange)
			VisitChange((NullChange)c);
		else if(c instanceof UndoTextFileChange)
			VisitChange((UndoTextFileChange)c);
		else if(c instanceof ResourceChange)
			VisitChange((ResourceChange)c);
		else
			return;
	}
	
	private void VisitChange( TextChange c)
	{
		RefactoringTextEditorVisitor visitor = 
			new RefactoringTextEditorVisitor();
		TextEditBasedChangeGroup[] group = c.getChangeGroups();
		for(int i = 0; i < group.length; i++)
		{
			TextEdit[] edits = group[i].getTextEdits();
			for(int j = 0; j < edits.length; j++)
				edits[j].accept(visitor);
		}
		animation.mergeAnimation(visitor.getAnimation());
		
	}
	private void VisitChange(NullChange c)
	{
		
	}
	
	private void VisitChange(UndoTextFileChange c)
	{
		
	}
	private void VisitChange(ResourceChange c)
	{
		
	}
}
