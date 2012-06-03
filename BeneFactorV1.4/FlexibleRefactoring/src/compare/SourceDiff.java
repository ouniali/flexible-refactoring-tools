package compare;

public interface SourceDiff {
	public int getLineNumber();
	public String performChange(String source);
	public String skipChange(String source);
	public String toString();
	public boolean isAtomic();
	public boolean isLineNumberAvailable();
	public boolean causeSourceChange();
	public boolean isAppliedSameLine(SourceDiff another);
}

abstract class SourceDiffAtomic implements SourceDiff{

	int lineNumber;
	
	protected SourceDiffAtomic(int l) {
		lineNumber = l;
	}
	
	public int getLineNumber()
	{
		return lineNumber;
	}

	@Override
	public final boolean isAtomic() {
		return true;
	}
	
	@Override
	public boolean isLineNumberAvailable()
	{
		return true;
	}
	
	@Override
	public boolean causeSourceChange()
	{
		return true;
	}
	
	public boolean isAppliedSameLine(SourceDiff another)
	{
		if(!another.isAtomic())
			return false;
		else if(!another.causeSourceChange())
			return true;
		else
			return another.getLineNumber() == this.getLineNumber();
	}
	
}

abstract class SourceDiffNotAtomic implements SourceDiff{

	
	protected SourceDiffNotAtomic()
	{
	}
	@Override
	public final boolean isAtomic() {
		return false;
	}
	
	public final int getLineNumber()
	{
		return -1;
	}
	
	@Override
	public final boolean isLineNumberAvailable()
	{
		return false;
	}
	
	@Override
	public boolean causeSourceChange()
	{
		return true;
	}
	
	public boolean isAppliedSameLine(SourceDiff another)
	{
		return false;
	}
}