package compare;

public class SourceDiffInsert extends SourceDiff{

	String insertedSource;
	
	public SourceDiffInsert(int l, String source) {
		super(l);
		insertedSource = source;
	}

	@Override
	String performUndo(String source) {
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "INSERT at " +getLineNumber() + '\n' + getInsertedCode();
	}
	
	public String getInsertedCode()
	{
		return insertedSource;
	}
}
