package compare;

public class SourceDiffChange extends SourceDiff{

	String beforeChange;
	String afterChange;
	
	public SourceDiffChange(int l, String from, String to) {
		super(l);
		beforeChange = from;
		afterChange = to;
	}

	@Override
	String performUndo(String source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "CHANGE AT" + getLineNumber()+ '\n' + 
			"before: \n" + getCodeBeforeChange() +
			"after: \n" + getCodeAfterChange();
	}
	
	public String getCodeBeforeChange()
	{
		return beforeChange;
	}
	
	public String getCodeAfterChange()
	{
		return afterChange;
	}
	
}
