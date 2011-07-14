package compare;

public class SourceDiffInsert extends SourceDiff{

	String insertedSource;
	
	public SourceDiffInsert(int l, String source) {
		super(l);
		insertedSource = source;
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



	@Override
	public String performChange(String source) {
		// TODO Auto-generated method stub
		return null;
	}
}
