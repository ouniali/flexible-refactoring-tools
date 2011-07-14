package compare;

import java.util.ArrayList;

public class SourceDiffInsert extends SourceDiff{

	ArrayList<String> insertedSource;
	
	public SourceDiffInsert(int l, ArrayList<String> source) {
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
		return combineStringArray(insertedSource);
	}



	@Override
	public String performChange(String source) {
		// TODO Auto-generated method stub
		return null;
	}
}
