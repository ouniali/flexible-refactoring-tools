package compare;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.compare.internal.*;

public class JavaSourceDiff {

	public static ArrayList<SourceDiff> getSourceDiffs(String oldPath,
			String newPath) {

		String des = Diff.getDiffDescription(oldPath, newPath);
		ArrayList<SourceDiff> diffs = new ArrayList<SourceDiff>();
		String[] lines = des.split("\n");

		diffs.addAll(getSourceDiffChanges(lines));
		diffs.addAll(getSourceDiffInserts(lines));
		diffs.addAll(getSourceDiffDeletes(lines));

		System.out.println(des);
		for (SourceDiff diff : diffs)
			System.out.println(diff);
		
		return diffs;
	}

	private static int extractIntegerFromString(String s) {
		Matcher matcher = Pattern.compile("\\d+").matcher(s);
		matcher.find();
		return Integer.valueOf(matcher.group());
	}

	private static ArrayList<SourceDiff> getSourceDiffChanges(String[] lines) {
		ArrayList<SourceDiff> changes = new ArrayList<SourceDiff>();
		ArrayList<Integer> change_from_lines = new ArrayList<Integer>();
		ArrayList<Integer> change_to_lines = new ArrayList<Integer>();
		String line;

		for (int i = 0; i < lines.length; i++) {
			line = lines[i];

			if (line.endsWith(Diff.TAIL_CHANGED_FROM)
					&& line.startsWith(Diff.HEADER_ARROW))
				change_from_lines.add(new Integer(i));
			else if (line.startsWith(Diff.HEADER_CHANGED_TO))
				change_to_lines.add(new Integer(i));
			else
				continue;
		}

		for (int i = 0; i < change_from_lines.size(); i++) {
			int fromLine = change_from_lines.get(i);
			int toLine = change_to_lines.get(i);
			changes.add(getSourceDiffChangeFromDiffDescription(lines, fromLine,
					toLine));
		}

		return changes;
	}

	private static SourceDiffChange getSourceDiffChangeFromDiffDescription(
			String[] lines, int fromLine, int toLine) {
		int lineNumber = extractIntegerFromString(lines[fromLine]);
		StringBuffer fromSource = new StringBuffer();
		StringBuffer toSource = new StringBuffer();

		for (int i = fromLine + 1;; i++) {
			if (lines[i].startsWith(Diff.HEADER_ARROW))
				break;
			else {
				fromSource.append(lines[i]);
				fromSource.append('\n');
			}
		}

		for (int i = toLine + 1;; i++) {
			if (lines[i].startsWith(Diff.HEADER_ARROW))
				break;
			else {
				toSource.append(lines[i]);
				toSource.append('\n');
			}
		}

		return new SourceDiffChange(lineNumber, fromSource.toString(),
				toSource.toString());
	}

	private static ArrayList<SourceDiff> getSourceDiffInserts(String[] lines) {
		ArrayList<SourceDiff> results = new ArrayList<SourceDiff>();
		ArrayList<Integer> insertLines = new ArrayList<Integer>();

		for (int i = 0; i < lines.length; i++) {
			if (lines[i].startsWith(Diff.HEADER_INSERT_BEFORE))
				insertLines.add(new Integer(i));
		}

		for (int i = 0; i < insertLines.size(); i++) {
			int insertLine = insertLines.get(i);
			results.add(getSourceDiffInsertFromDiffDescription(lines,
					insertLine));
		}

		return results;
	}

	private static ArrayList<SourceDiff> getSourceDiffDeletes(String[] lines) {
		ArrayList<SourceDiff> results = new ArrayList<SourceDiff>();
		ArrayList<Integer> deleteLines = new ArrayList<Integer>();

		for (int i = 0; i < lines.length; i++) {
			if (lines[i].startsWith(Diff.HEADER_DELETE_AT))
				deleteLines.add(new Integer(i));
		}

		for (int i = 0; i < deleteLines.size(); i++) {
			int deleteLine = deleteLines.get(i);
			results.add(getSourceDiffDeleteFromDiffDescription(lines,
					deleteLine));
		}

		return results;
	}

	private static SourceDiffInsert getSourceDiffInsertFromDiffDescription(
			String[] lines, int insertLine) {
		StringBuffer source = new StringBuffer();
		int lineNumber = extractIntegerFromString(lines[insertLine]);
		for (int i = insertLine + 1;; i++) {
			if (lines[i].startsWith(Diff.HEADER_ARROW))
				break;
			else {
				source.append(lines[i]);
				source.append('\n');
			}
		}
		return new SourceDiffInsert(lineNumber, source.toString());
	}

	private static SourceDiffDelete getSourceDiffDeleteFromDiffDescription(
			String[] lines, int deleteLine) {
		StringBuffer source = new StringBuffer();
		int lineNumber = extractIntegerFromString(lines[deleteLine]);
		for (int i = deleteLine + 1;; i++) {
			if (lines[i].startsWith(Diff.HEADER_ARROW))
				break;
			else {
				source.append(lines[i]);
				source.append('\n');
			}
		}
		return new SourceDiffDelete(lineNumber, source.toString());
	}

}
