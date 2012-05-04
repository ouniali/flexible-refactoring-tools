package compare;

public class SourceDiffNull  extends SourceDiff{

	public SourceDiffNull(int l) {
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
		return "SourceDiffNull";
	}

}
