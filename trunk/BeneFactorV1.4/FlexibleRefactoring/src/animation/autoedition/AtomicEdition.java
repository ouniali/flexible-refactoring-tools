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
import org.eclipse.text.edits.UndoEdit;

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
		TextEdit original = TextEditUtil.deepCopy(edit);
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
		return splitEdit(edit);
	}
	
	private static ArrayList<AtomicEdition> splitEdit(TextEdit e) throws Exception
	{
		ArrayList<AtomicEdition> eds = new ArrayList<AtomicEdition>();
		if(e instanceof ReplaceEdit)
			eds = splitEdit((ReplaceEdit)e);
		else if (e instanceof InsertEdit)
			eds = splitEdit((InsertEdit)e);
		else if(e instanceof DeleteEdit)
			eds = splitEdit((DeleteEdit)e);
		else if (e instanceof UndoEdit)
			eds = splitEdit((UndoEdit)e);
		else 
			throw new Exception("Unknown Edit Type.");	
		return eds;
	}
	private static ArrayList<AtomicEdition> splitEdit(ReplaceEdit e)
	{
		ArrayList<AtomicEdition> eds = new ArrayList<AtomicEdition>();
		DeleteEdit del = new DeleteEdit(e.getOffset(), e.getLength());
		eds.add(new AtomicEdition(del));
		eds.addAll(splitLongString(e.getOffset(), e.getText()));
		return eds;
	}
	private static ArrayList<AtomicEdition> splitEdit(InsertEdit e)
	{
		ArrayList<AtomicEdition> eds = new ArrayList<AtomicEdition>();
		eds.addAll(splitLongString(e.getOffset(), e.getText()));
		return eds;
	}
	private static ArrayList<AtomicEdition> splitEdit(DeleteEdit e)
	{
		ArrayList<AtomicEdition> eds = new ArrayList<AtomicEdition>();
		AtomicEdition edition = new AtomicEdition(e);
		edition.undo = null;
		eds.add(edition);
		return eds;
	}
	
	private static ArrayList<AtomicEdition> splitEdit(UndoEdit e) throws Exception
	{
		ArrayList<AtomicEdition> editions = new ArrayList<AtomicEdition>();
		for(int i = 0; i < e.getChildrenSize(); i++)
			editions.add(new AtomicEdition(e.getChildren()[i]));
		return topDown2BottomUp(editions);
	}
	
	private static ArrayList<AtomicEdition> topDown2BottomUp(ArrayList<AtomicEdition> top_downs) throws Exception
	{		
		ArrayList<AtomicEdition> bottom_ups = new ArrayList<AtomicEdition>();
		int shift = 0;
		for(int i = top_downs.size() - 1; i >= 0; i--)
		{
			AtomicEdition current = top_downs.get(i);
			current.setOffset(current.getOffset() - shift);
			shift = shift + current.getRangeChange();
			bottom_ups.add(0, current);			
		}
		return bottom_ups;		
		
	}
	
	 private static ArrayList<AtomicEdition> splitLongString(int off, String s)
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
		 edit = changeOffset(edit, off);
	 }
	 
	 private static TextEdit changeOffset(TextEdit e, int off) throws Exception
	 {
		 if(e.getClass().equals(ReplaceEdit.class))
		 {
			 return new ReplaceEdit(off, ((ReplaceEdit) e).getLength(),((ReplaceEdit) e).getText());
		 }
		 else if (e instanceof InsertEdit)
		 {
			 return new InsertEdit(off,((InsertEdit) e).getText());
		 }
		 else if(e instanceof DeleteEdit)
		 {
			 return new DeleteEdit(off, e.getLength());
		 }
		 else if (e instanceof UndoEdit)
		 {
			 if(e.getChildren().length > 1)
				 throw new Exception("More than one child.");
			 TextEdit child = e.getChildren()[0];
			 TextEdit new_child = changeOffset(child, off);
			 return new_child;
		 }
		 else
			 throw new Exception("Unkown Edit Type:" + e);
		 
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
		int o1 = edit.getOffset();
		int o2 = ((AtomicEdition)e).edit.getOffset();
		return o1 - o2;
	}

	static public AtomicEdition mergeConsecutiveAtomicEditionsTop2Bottom
		(ArrayList<AtomicEdition> editions, int start, int end) throws Exception
	{		
		MultiTextEdit combined = new MultiTextEdit();
		int off_adjust = 0;
		for(int i = start; i <= end; i++)
		{
			AtomicEdition current = (AtomicEdition)editions.get(i).clone();
			current.setOffset(current.getOffset() - off_adjust);
			combined.addChild(current.edit);
			off_adjust += current.getRangeChange();	
		}
		return new AtomicEdition(combined);
	}
	
	static public AtomicEdition mergeConsecutiveAtomicEditionsBottom2Top
		(ArrayList<AtomicEdition> editions, int start, int end) throws Exception
	{			
		ArrayList<TextEdit> edits = mergeOverlappedTextEdit(toTextEditList(editions, start, end));
		MultiTextEdit multi = new MultiTextEdit();
		for(TextEdit e:edits)
			multi.addChild(e);
		return new AtomicEdition(multi);
	}
	
	private static ArrayList<TextEdit> toTextEditList(ArrayList<AtomicEdition> atoms, int start, int end)
	{
		ArrayList<TextEdit> edits = new ArrayList<TextEdit>();
		for(int i = start; i <= end; i++)
			edits.add(atoms.get(i).edit);
		return edits;
	}
	
	private static ArrayList<TextEdit> mergeOverlappedTextEdit(ArrayList<TextEdit> edits) throws Exception
	{
		ArrayList<TextEdit> new_edits = new ArrayList<TextEdit>();
		int start = 0;
		while(start < edits.size())
		{
			int end = TextEditUtil.getIndexofSameOffset(edits, start);
			new_edits.add(TextEditUtil.mergeEdits(edits, start, end));
			start = end + 1;
		}
		return new_edits;
	}
	
	public Object clone()
	{
		AtomicEdition ae = new AtomicEdition (edit.copy());
		if(undo != null)
			ae.undo = undo.copy();
		return ae;	
	}


}
