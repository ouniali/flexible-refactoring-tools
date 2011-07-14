package compare;

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
	abstract String performUndo(String source);
}
