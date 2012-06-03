package compare;

public interface SourceDiff {
	public int getLineNumber();
	public String performChange(String source);
	public String skipChange(String source);
	public String toString();
	public boolean isAtomic();
	public boolean isLineNumberAvailable();
	public boolean causeSourceChange();
	public boolean isSameLine(SourceDiff another);
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
	
	public boolean isSameLine(SourceDiff another)
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

	@Override
	public final boolean isAtomic() {
		return false;
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
	
	public boolean isSameLine(SourceDiff another)
	{
		return false;
	}
}