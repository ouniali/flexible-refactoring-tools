package animation.change;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Stack;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IWorkingCopy;
import org.eclipse.jdt.core.JavaModelException;
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

import animation.Animation;
import animation.autoedition.SingleFileEdition;

public class ChangeAnalyzer{
	
	Change change;
	TextVisitorStrategy strategy;
	ArrayList results;
	
	public ArrayList getResults() {
		return results;
	}


	public ChangeAnalyzer(Change c, TextVisitorStrategy s) throws Exception
	{
		strategy = s;
		change = c;
		results = new ArrayList();
		if(change instanceof CompositeChange)
			VisitCompositeChangeTree((CompositeChange)change);		
	}
	

	private void VisitCompositeChangeTree(CompositeChange cc) throws Exception
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
	
	private void VisitChange(Change c) throws Exception {
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
		ICompilationUnit unit = (ICompilationUnit)c.getModifiedElement();
		TextEditBasedChangeGroup[] group = c.getChangeGroups();
		TextEditVisitor visitor = strategy.getTextEditVisitor();
		strategy.setEnvironment(visitor, unit);
		
		for(int i = 0; i < group.length; i++)
		{
			TextEdit[] edits = group[i].getTextEdits();
			for(int j = 0; j < edits.length; j++)
				edits[j].accept(visitor);
		}
		
		results.add(strategy.getResult(visitor));		
	}
	
	private void VisitChange(ResourceChange c) throws Exception
	{
		throw new Exception("Not implemented");
	}
	
	private void VisitChange(UndoTextFileChange c) throws Exception
	{
		throw new Exception("Not implemented");
	}

	private void VisitChange(NullChange c) throws Exception
	{
		throw new Exception("Not implemented");
	}


	
	public static interface TextVisitorStrategy{
		public TextEditVisitor getTextEditVisitor();
		public Object getResult(TextEditVisitor visitor);
		public void setEnvironment(TextEditVisitor visitor, ICompilationUnit u);
	}
	
	public static class AutoEditionVisitorStrategy implements TextVisitorStrategy
	{
		public TextEditVisitor getTextEditVisitor() {
			return new AutoEditionVisitor();
		}

		public Object getResult(TextEditVisitor visitor) {
			SingleFileEdition com = ((AutoEditionVisitor)visitor).getComposite();	
			return com;
		}

		public void setEnvironment(TextEditVisitor visitor, ICompilationUnit u) {
			((AutoEditionVisitor)visitor).setEnvironment(u);
		}	
	}
	
	
	
	public static class AnimatedChangeVisitorStrategy implements TextVisitorStrategy
	{
		public TextEditVisitor getTextEditVisitor() {
			return new AnimatedChangeVisitor();
		}
		public Object getResult(TextEditVisitor visitor) {
			return null;
		}
		public void setEnvironment(TextEditVisitor visitor, ICompilationUnit u) {
			
			
		}
		
	}

	
	
	
	
	
	
	
	
}
