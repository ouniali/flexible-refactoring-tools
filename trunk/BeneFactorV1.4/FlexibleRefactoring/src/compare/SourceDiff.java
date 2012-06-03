package compare;

abstract public interface SourceDiff {

	public int getLineNumber();
	public String performChange(String source);
	public String skipChange(String source);
	public String toString();
	public boolean isAtomic();

}

abstract class SourceDiffAtomic implements SourceDiff{

	int lineNumber;
	
	public SourceDiffAtomic(int l) {
		lineNumber = l;
	}
	
	public final int getLineNumber()
	{
		return lineNumber;
	}

	@Override
	public final boolean isAtomic() {
		return true;
	}
	
}

abstract class SourceDiffNotAtomic implements SourceDiff{

	@Override
	public final boolean isAtomic() {
		return false;
	}
}