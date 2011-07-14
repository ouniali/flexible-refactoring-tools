package compare;

public class SourceDiffDelete extends SourceDiff{

	String deletedSource;
	
	public SourceDiffDelete(int l, String dCode) {
		super(l);
		deletedSource = dCode;
	}

	@Override
	String performUndo(String source) {
		return null;
	}

}
