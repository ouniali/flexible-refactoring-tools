package animation.autoedition;

import org.eclipse.text.edits.ReplaceEdit;

public class TextEditUtil {

	public static ReplaceEdit mergeReplaceEdit(ReplaceEdit before, ReplaceEdit after) throws Exception
	{
		if(before.getOffset() != after.getOffset())
			throw new Exception("unmergable replace edit.");
		
		String s1 = before.getText();
		String s2 = before.getText();
		String final_string;
		if(s1.length() > s2.length())
			final_string = s2 + s1.substring(s2.length(), s1.length() - 1);
		else
			final_string = s2;
			
		if(before.getLength() > s1.length())
		{
			
		}
		else if(before.getLength() < s1.length())
		{
			
		}
		{
			
		}
		return null;
	}
	
}
