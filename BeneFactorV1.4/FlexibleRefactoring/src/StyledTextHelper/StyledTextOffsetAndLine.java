package StyledTextHelper;

import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.swt.custom.StyledText;

public class StyledTextOffsetAndLine extends StyledTextHelper{

	JavaEditor editor;
	int lineNumber;
	int offset;
	boolean input_offset;
	
	public StyledTextOffsetAndLine(int input, boolean isOff, JavaEditor e)
	{
		input_offset = isOff;
		editor = e;
		if(input_offset)
			offset = input;
		else
			lineNumber = input;
	}
	
	public void run()
	{
		StyledText st = getStyledTextHelper(editor);
		if(input_offset)
			lineNumber = st.getLineAtOffset(offset);
		else
			offset = st.getOffsetAtLine(lineNumber);
	}
	
	public int getLineNumber() {
		return lineNumber;
	}

	public int getOffset() {
		return offset;
	}

}
