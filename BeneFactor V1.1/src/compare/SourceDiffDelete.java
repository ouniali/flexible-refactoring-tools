package compare;

import java.util.ArrayList;

public class SourceDiffDelete extends SourceDiff {

	ArrayList<String> deletedSource;

	public SourceDiffDelete(int l, ArrayList<String> dCode) {
		super(l);
		deletedSource = dCode;
	}

	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "DELETE at " + getLineNumber() + '\n' + getDeletedCode();
	}

	public String getDeletedCode() {
		return combineStringArray(deletedSource);
	}



	@Override
	public String performChange(String source) {
		// TODO Auto-generated method stub
		return null;
	}

}
