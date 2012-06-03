package compare;

public class SourceDiffIdentical extends SourceDiffAtomic{

	public SourceDiffIdentical(int l) {
		super(l);
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
	
	@Override
	public boolean causeSourceChange()
	{
		return false;
	}

}
