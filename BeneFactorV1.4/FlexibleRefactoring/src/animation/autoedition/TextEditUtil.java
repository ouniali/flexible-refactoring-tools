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
			throw new Exception("unmergable replace edit.");
		
	
	
			
		
		return null;
	}
	
	public static void main(String arg[]) throws Exception
	{
		  IDocument document= new Document("org");
          MultiTextEdit edit= new MultiTextEdit();
          edit.addChild(new DeleteEdit(0, 1));
          MultiTextEdit sub = new MultiTextEdit(); 
          sub.addChild(new DeleteEdit(0, 1));
          edit.addChild(sub);
          edit.apply(document);
          System.out.println(document.get());
	}
	
}
