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
		if(before.getOffset() != after.getOffset())
			throw new Exception("Unmergable replace edit. Before: " + before + "; After: " + after);
		if(0 != after.getLength())
			throw new Exception("Unmergable replace edit.");
		
		String new_text = after.getText() + before.getText();
		return new ReplaceEdit(before.getOffset(), before.getLength(), new_text);
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
