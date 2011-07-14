package compare;

import java.util.ArrayList;

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
		return combineStringArray(beforeChange);
	}
	
	public String getCodeAfterChange()
	{
		return combineStringArray(afterChange);
	}



	@Override
	public String performChange(String source) {
		// TODO Auto-generated method stub
		String[] lines = source.split("\n");
		
		
		return null;
	}
	
}
