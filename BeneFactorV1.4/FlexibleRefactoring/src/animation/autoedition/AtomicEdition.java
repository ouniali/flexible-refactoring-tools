package animation.autoedition;

import java.util.ArrayList;
import java.util.Comparator;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Display;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;

public class AtomicEdition implements Comparable{
		
	TextEdit edit;
	TextEdit undo;
	
	public AtomicEdition(int offset, int length)
	{
		edit = new DeleteEdit(offset, length);
	}
	
	public AtomicEdition(int offset, String s)
	{
		edit = new InsertEdit(offset, s);
	}
	
	public AtomicEdition(int offset, int length, String s)
	{
		edit = new ReplaceEdit(offset, length, s);
	}
	
	public AtomicEdition(TextEdit e)
	{
		edit = e;
	}
	
	public void applyEdition(ICompilationUnit unit) throws Exception
	{
		TextEdit original = edit.copy();
		undo = unit.applyTextEdit(edit, null);
		edit = original;	
	}
	
	public AtomicEdition getUndoAtomicEdition()
	{
		if(undo != null)
			return new AtomicEdition(undo);
		else 
			return null;
	}
	
	public String toString()
	{
		return edit.toString();
	}
	
	public ArrayList<AtomicEdition> splitToAtomicEditions() throws Exception
	{
		ArrayList<AtomicEdition> eds = new ArrayList<AtomicEdition>();
		
		if(edit instanceof ReplaceEdit)
		{
			 DeleteEdit del = new DeleteEdit(edit.getOffset(), edit.getLength());
			 eds.add(new AtomicEdition(del));
			 eds.addAll(splitLongString(edit.getOffset(), ((ReplaceEdit) edit).getText()));
		}
		else if (edit instanceof InsertEdit)
		{
			eds.addAll(splitLongString(edit.getOffset(), ((InsertEdit) edit).getText()));
		}
		else if(edit instanceof DeleteEdit)
		{
			undo = null;
			eds.add(this);
		}
		else 
			throw new Exception("Unknown Edit Type.");
		
		return eds;
	}
	
	 private ArrayList<AtomicEdition> splitLongString(int off, String s)
	 {
		 char[] chars = s.toCharArray();
		 ArrayList<AtomicEdition> editions = new ArrayList<AtomicEdition>();
		 for(char c : chars)
		 {
			 editions.add(new AtomicEdition(off, String.valueOf(c)));
			 off++;
		 }	 
		 return editions;
	 }
	
	 
	 public int getOffset()
	 {
		 return edit.getOffset();
	 }
	
	 public void setOffset(int off) throws Exception
	 {
		 undo = null;
		 
		 if(edit instanceof ReplaceEdit)
		 {
			 edit = new ReplaceEdit(off, ((ReplaceEdit) edit).getLength(),((ReplaceEdit) edit).getText());
		 }
		 else if (edit instanceof InsertEdit)
		 {
			 edit = new InsertEdit(off, ((InsertEdit) edit).getText());
		 }
		 else if(edit instanceof DeleteEdit)
		 {
			 edit = new DeleteEdit(off, edit.getLength());
		 }
		 else
			 throw new Exception("Unkown Edit Type.");
	 }
	 
	 
	 public int getRangeChange() throws Exception
	 {
		 int[] range = new int[2];
		 if(edit instanceof ReplaceEdit)
		 {
			 range[0] = edit.getLength();
			 range[1] = ((ReplaceEdit) edit).getText().length();
		 }
		 else if (edit instanceof InsertEdit)
		 {
			 range[0] = 0;
			 range[1] = ((InsertEdit) edit).getText().length();
		 }
		 else if(edit instanceof DeleteEdit)
		 {
			 range[0] = ((DeleteEdit)edit).getLength();
			 range[1] = 0;
		 }
		 else
			 throw new Exception("Unkown Edit Type.");
		 
		 return range[1] - range[0];// >0 for expand; <0 for shrink
	 }

	@Override
	public int compareTo(Object e) {
		// TODO Auto-generated method stub
		int o1 = edit.getOffset();
		int o2 = ((AtomicEdition)e).edit.getOffset();
		return o1 - o2;
	}


}
