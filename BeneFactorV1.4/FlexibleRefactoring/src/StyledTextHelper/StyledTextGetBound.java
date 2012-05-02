package StyledTextHelper;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public class StyledTextGetBound extends StyledTextHelper {

	int start;
	int end;
	JavaEditor editor;
	Rectangle bound;
	
	public StyledTextGetBound(int s, int e, JavaEditor ed)
	{
		start = s;
		end = e;
		editor = ed;
	}
	
	public void run()
	{
		StyledText st = getStyledTextHelper(editor);
		bound = st.getTextBounds(start, end);
		Point p = st.toDisplay(bound.x, bound.y);
		bound = new Rectangle(p.x, p.y, bound.width, bound.height);
	}
	
	public Rectangle getTextBound()
	{
		return bound;
	}
}
