package StyledTextHelper;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.swt.custom.StyledText;

public class StyledTextGetLineHeight extends StyledTextHelper{
	
	int offset;
	JavaEditor editor;
	int height;
	
	public StyledTextGetLineHeight(int o, JavaEditor e)
	{
		offset = o;
		editor = e;
	}
	public void run()
	{
		StyledText st = getStyledTextHelper(editor);
		height = st.getLineHeight(offset);
	}

	public int getLineHeight()
	{
		return height;
	}
}
