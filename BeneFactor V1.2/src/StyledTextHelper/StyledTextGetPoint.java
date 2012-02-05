package StyledTextHelper;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;

public class StyledTextGetPoint extends StyledTextHelper{
	
	int offset;
	JavaEditor editor;
	Point position;
	
	public StyledTextGetPoint(int o, JavaEditor e)
	{
		editor = e;
		offset = o;
	}

	public void run()
	{
		StyledText st = getStyledTextHelper(editor);
		position = st.toDisplay(st.getLocationAtOffset(offset));
	}
	
	public Point getPositionToDisplay()
	{
		return position;
	}
}
