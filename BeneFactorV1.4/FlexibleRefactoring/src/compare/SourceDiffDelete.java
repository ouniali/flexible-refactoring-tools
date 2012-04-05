package compare;

import java.util.ArrayList;

import utitilies.StringUtilities;

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
		return StringUtilities.combineStringArray(deletedSource);
	}

	@Override
	public String performChange(String source) {
		// TODO Auto-generated method stub
		int deleteAt = getLineNumber();
		String[] lines = source.split("\n");
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < deleteAt - 1; i++) {
			result.append(lines[i]);
			result.append('\n');
		}

		for (int i = deleteAt + deletedSource.size() - 1; i < lines.length; i++) {
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
