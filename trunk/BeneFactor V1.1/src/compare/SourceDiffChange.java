package compare;

import java.util.ArrayList;

import utitilies.StringUtilities;

public class SourceDiffChange extends SourceDiff{

	ArrayList<String> beforeChange;
	ArrayList<String> afterChange;
	
	public SourceDiffChange(int l, ArrayList<String> from, ArrayList<String> to) {
		super(l);
		beforeChange = from;
		afterChange = to;
	}



	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "CHANGE at " + getLineNumber()+ '\n' + 
			"before: \n" + getCodeBeforeChange() +
			"after: \n" + getCodeAfterChange();
	}
	
	public String getCodeBeforeChange()
	{
		return StringUtilities.combineStringArray(beforeChange);
	}
	
	public String getCodeAfterChange()
	{
		return StringUtilities.combineStringArray(afterChange);
	}



	@Override
	public String performChange(String source) {
		// TODO Auto-generated method stub
		int changeLineAt = getLineNumber(); 
		String[] lines = source.split("\n");
		StringBuffer result = new StringBuffer();
		
		for(int i = 0 ; i< changeLineAt - 1; i++)
		{
			result.append(lines[i]);
			result.append('\n');
		}
		
		for(String s : afterChange)
		{
			result.append(s);
			result.append('\n');
		}
		
		for(int i = changeLineAt + beforeChange.size() -1; i< lines.length; i++)
		{
			result.append(lines[i]);
			result.append('\n');
		}	
		
		return result.toString();
	}	

	@Override
	public String skipChange(String source) {
		// TODO Auto-generated method stub
		return source;
	}
	
}
