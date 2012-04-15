package animation.autoedition;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.DeleteEdit;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;

public class TextEditUtil {

	
	
	public static ReplaceEdit mergeReplaceEdit(ReplaceEdit before, ReplaceEdit after) throws Exception
	{
		if(before.getOffset() != after.getOffset() || 0 != after.getLength())
			throw new Exception("Unmergable replace edit. Before: " + before + "; After: " + after + ";");
		if(after.getLength() == 0 )
			return AfterAsInsert(before, after);
		else if(after.getText().length() == 0)
			return AfterAsDelete(before, after);	
		else 
			throw new Exception("Unmergable replace edit. Before: " + before + "; After: " + after + ";");
	}

	private static ReplaceEdit AfterAsDelete(ReplaceEdit before, ReplaceEdit after) {
		String new_text;
		int new_length;
		if(after.getLength() <= before.getText().length())
		{
			new_text = before.getText().substring(after.getLength(), before.getText().length() - 1);
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
	
	public static void main(String arg[]) throws Exception
	{
		  IDocument document= new Document("org");
          MultiTextEdit edit= new MultiTextEdit();
          edit.addChild(new ReplaceEdit(0, 0, ""));
          MultiTextEdit sub = new MultiTextEdit(); 
          sub.addChild(new ReplaceEdit(0, 1, ""));
          edit.addChild(sub);
          edit.apply(document);
          System.out.println(document.get());
	}
	
}
