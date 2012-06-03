package compare;

public class SourceDiffMove extends SourceDiffAtomic {

	public SourceDiffMove(int l) {
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
		return "Move";
	}

}
