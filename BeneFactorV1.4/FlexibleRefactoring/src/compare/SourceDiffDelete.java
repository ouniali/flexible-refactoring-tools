package compare;

import java.util.ArrayList;

import util.StringUtil;

public class SourceDiffDelete extends SourceDiffAtomic {

	ArrayList<String> deletedSource;

	public SourceDiffDelete(int l, ArrayList<String> dCode) {
		super(l);
		deletedSource = dCode;
	}

	@Override
	public String toString() {
		return "DELETE at " + getLineNumber() + '\n' + getDeletedCode();
	}

	public String getDeletedCode() {
		return StringUtil.combineStringArray(deletedSource);
	}

	@Override
	public String performChange(String source) {
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
		return source;
	}


}
