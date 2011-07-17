package compare;

import java.util.ArrayList;

abstract public class SourceDiff {
	int lineNumber;
	public SourceDiff(int l)
	{
		lineNumber = l;
	}
	public int getLineNumber()
	{
		return lineNumber;
	}
	public abstract String performChange(String source);
	public abstract String skipChange(String source);
	public abstract String toString();

}
