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
	
}
