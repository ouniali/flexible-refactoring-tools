package compare;

public class SourceDiffIdentical extends SourceDiffAtomic{

	private static SourceDiffIdentical instance;
	
	private SourceDiffIdentical() {
		super(0);
	}
	
	public static SourceDiffIdentical getInstance()
	{
		if(instance == null)
			instance = new SourceDiffIdentical();
		return instance;
	}

	@Override
	public String performChange(String source) {
			return source;
	}

	@Override
	public String skipChange(String source) {
		return source;
	}

	@Override
	public String toString() {
		return "Identical";
	}

	@Override
	public boolean isLineNumberAvailable()
	{
		return false;
	}
	
	public int getLineNumber()
	{
		return -1;
	}
	
	@Override
	public boolean causeSourceChange()
	{
		return false;
	}

}
