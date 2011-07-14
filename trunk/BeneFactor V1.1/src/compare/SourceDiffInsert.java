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
		int insertBefore = getLineNumber();
		String[] lines = source.split("\n");
		StringBuffer result = new StringBuffer();
		
		for(int i = 0; i < insertBefore-1; i++)
		{
			result.append(lines[i]);
			result.append('\n');
		}
		for(int i = 0; i< insertedSource.size(); i++)
		{
			result.append(insertedSource.get(i));
			result.append('\n');
		}
		for(int i = insertBefore -1; i<lines.length; i++)
		{
			result.append(lines[i]);
			result.append('\n');
		}
		
		return result.toString();
	}
}
