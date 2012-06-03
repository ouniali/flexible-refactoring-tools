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
	public abstract String performChange(String source);
	public abstract String skipChange(String source);
	public abstract String toString();
	public abstract boolean isAtomic();

}

abstract class SourceDiffAtomic extends SourceDiff{

	public SourceDiffAtomic(int l) {
		super(l);
	}

	@Override
	public final boolean isAtomic() {
		return true;
	}
	
}

abstract class SourceDiffNotAtomic extends SourceDiff{

	public SourceDiffNotAtomic() {
		super(0);
	}
	@Override
	public final boolean isAtomic() {
		return false;
	}
}