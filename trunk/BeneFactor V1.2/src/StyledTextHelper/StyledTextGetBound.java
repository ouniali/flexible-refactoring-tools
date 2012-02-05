package StyledTextHelper;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Rectangle;

public class StyledTextGetBound extends StyledTextHelper {

	int start;
	int end;
	JavaEditor editor;
	Rectangle bounds;
	
	public StyledTextGetBound(int s, int e, JavaEditor ed)
	{
		start = s;
		end = e;
		editor = ed;
	}
	
	public void run()
	{
		StyledText st = getStyledTextHelper(editor);
		bounds = st.getTextBounds(start, end);
	}
	
	public Rectangle getTextBound()
	{
		return bounds;
	}
}
