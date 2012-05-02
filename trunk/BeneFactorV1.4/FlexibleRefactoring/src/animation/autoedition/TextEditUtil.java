package animation.autoedition;

import java.util.ArrayList;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;

public class TextEditUtil {

	
	
	public static ReplaceEdit mergeReplaceEdit(ReplaceEdit before, ReplaceEdit after) throws Exception
	{
		System.out.println("Before: " + before);
		System.out.println("After: " + after);
		if(before.getOffset() != after.getOffset())
			throw new Exception("Unmergable replace edit. Before: " + before + "; After: " + after + ";");
		if(0 == after.getLength() )
		{
			ReplaceEdit res =  AfterAsInsert(before, after);
			System.out.println("Result: " + res);
			return res;
		}
			
		else if(0 == after.getText().length())
		{
			ReplaceEdit res = AfterAsDelete(before, after);
			System.out.println("Result: " + res);
			return res;
		}
		else 
			throw new Exception("Unmergable replace edit. Before: " + before + "; After: " + after + ";");
	}

	private static ReplaceEdit AfterAsDelete(ReplaceEdit before, ReplaceEdit after) 
	{
		String new_text;
		int new_length;
		if(after.getLength() <= before.getText().length())
		{
			new_text = before.getText().substring(after.getLength());
			new_length = before.getLength();
		}
		else
		{
			new_text = "";
			new_length = before.getLength() + (after.getLength() - before.getText().length());
		}
		
		return new ReplaceEdit(before.getOffset(), new_length, new_text);
	}

	private static ReplaceEdit AfterAsInsert(ReplaceEdit before, ReplaceEdit after) {
		String new_text;
		int new_length;
		new_text = after.getText() + before.getText();
		new_length = before.getLength();
		return new ReplaceEdit(before.getOffset(), new_length, new_text);
	}
	
	
	public static TextEdit transformReplaceEdit(ReplaceEdit edit) throws Exception
	{
		if(0 == edit.getText().length())
			return new DeleteEdit(edit.getOffset(), edit.getLength());
		else if (0 == edit.getLength())
			return new InsertEdit(edit.getOffset(), edit.getText());
		else
			throw new Exception("Untransformable replace edit.");
	}
	
	
	public static int getIndexofSameOffset(ArrayList<TextEdit> edits, int start)
	{
		int off = edits.get(start).getOffset();
		for(int i = start + 1; i < edits.size(); i++)
		{
			if(off != edits.get(i).getOffset())
				return i - 1;
		}
		return edits.size() - 1;
	}
	
	public static TextEdit mergeEdits(ArrayList<TextEdit> edits, int start, int end) throws Exception
	{
		ReplaceEdit combined = getReplaceEdit(edits.get(start));
		for(int i = start + 1; i <= end; i++)
		{
			ReplaceEdit re = getReplaceEdit(edits.get(i));
			combined = TextEditUtil.mergeReplaceEdit(combined, re);
		}
		return combined;
	}
	
	private static ReplaceEdit getReplaceEdit(TextEdit e) throws Exception
	{
		if(e instanceof UndoEdit)
		{
			if(e.getChildrenSize() > 1)
				throw new Exception("UndoEdit has more than one child.");
			return (ReplaceEdit) e.getChildren()[0];
		}
		else if(e instanceof ReplaceEdit)
			return (ReplaceEdit)e;
		else
			throw new Exception("Cannot covert to ReplaceEdit.");
	}
	
	
	public static TextEdit deepCopy(TextEdit root)
	{
		if(root instanceof MultiTextEdit)
		{
			MultiTextEdit new_root = new MultiTextEdit();
			for(TextEdit e : root.getChildren())
				new_root.addChild(deepCopy(e));
			return new_root;
		}
		else
			return root.copy();
		
	}
	
	
	
	
	public static void main(String arg[]) throws Exception
	{
		  IDocument document= new Document("org");
          MultiTextEdit edit= new MultiTextEdit();
          edit.addChild(new InsertEdit(0, "1"));
          edit.addChild(new InsertEdit(0, "1"));
          edit.addChild(new InsertEdit(1, "1"));
          TextEdit undo = edit.apply(document);
          System.out.println(document.get());
          System.out.println(undo);
	}
	
}
