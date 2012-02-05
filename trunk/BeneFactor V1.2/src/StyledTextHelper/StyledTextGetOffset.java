package StyledTextHelper;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;

public class StyledTextGetOffset extends StyledTextHelper{
	
	Point position;
	JavaEditor editor;
	int offset;
	
	StyledTextGetOffset(Point p, JavaEditor e)
	{
		position = p;
		editor = e;
	}
	public void run()
	{
		StyledText st = getStyledTextHelper(editor);
		offset = st.getOffsetAtLocation(position);
	}
	public int getOffset()
	{
		return offset;
	}
}
