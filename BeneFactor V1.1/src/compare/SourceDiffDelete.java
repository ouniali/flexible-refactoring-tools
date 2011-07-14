package compare;

public class SourceDiffDelete extends SourceDiff {

	String deletedSource;

	public SourceDiffDelete(int l, String dCode) {
		super(l);
		deletedSource = dCode;
	}

	@Override
	String performUndo(String source) {
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "DELETE at " + getLineNumber() + '\n' + getDeletedCode();
	}

	public String getDeletedCode() {
		return deletedSource;
	}

}
