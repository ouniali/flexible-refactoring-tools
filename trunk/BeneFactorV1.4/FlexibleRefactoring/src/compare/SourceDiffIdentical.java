package compare;

public class SourceDiffIdentical extends SourceDiff{

	public SourceDiffIdentical(int l) {
		super(l);
	}

	@Override
	public String performChange(String source) {
		// TODO Auto-generated method stub
		return source;
	}

	@Override
	public String skipChange(String source) {
		// TODO Auto-generated method stub
		return source;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Identical";
	}

}
