package compare;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.compare.internal.*;

import compare.diff_match_patch.Patch;

import util.*;

public class JavaSourceDiff {

	public static SourceDiff getSourceDiff(String oldPath, String newPath) {

		String des = Diff.getDiffDescription(oldPath, newPath);
		ArrayList<SourceDiff> diffs = new ArrayList<SourceDiff>();
		String[] lines = des.split("\n");
		diffs.addAll(getSourceDiffChanges(lines));
		diffs.addAll(getSourceDiffInserts(lines));
		diffs.addAll(getSourceDiffDeletes(lines));
		if(diffs.isEmpty())
			diffs.add(SourceDiffIdentical.getInstance());
		return combineSourceDiffs(diffs);
	}

	private static SourceDiff combineSourceDiffs(List<SourceDiff> diffs)
	{
		if(diffs.size() == 1)
			return diffs.get(0);
		else
			return new SourceDiffMulti(diffs);
	}
	
	
	
	
	public static LinkedList<Patch> getPatches(String olds, String news)
	{
		diff_match_patch dmp= new diff_match_patch();	
		return dmp.patch_make(olds, news);
	}
	
	public static String applyPatches(String text, LinkedList<Patch> patches)
	{
		Object things[] = new diff_match_patch().patch_apply(patches, text);
		String newText = (String)things[0];
		boolean[] results = (boolean[]) things[1];
		return newText;
		
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
		int lineNumber = StringUtil.extractIntegerFromString(lines[fromLine]);
		ArrayList<String> fromSource = new ArrayList<String>();
		ArrayList<String> toSource = new ArrayList<String>();

		for (int i = fromLine + 1;; i++) {
			if (lines[i].startsWith(Diff.HEADER_ARROW))
				break;
			else 
				fromSource.add(lines[i]);
		}

		for (int i = toLine + 1;; i++) {
			if (lines[i].startsWith(Diff.HEADER_ARROW))
				break;
			else 
				toSource.add(lines[i]);
		}

		return new SourceDiffChange(lineNumber, fromSource,
				toSource);
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
		ArrayList<String> source = new ArrayList<String>();
		int lineNumber = StringUtil.extractIntegerFromString(lines[insertLine]);
		for (int i = insertLine + 1;; i++) {
			if (lines[i].startsWith(Diff.HEADER_ARROW))
				break;
			else 
				source.add(lines[i]);
		}
		return new SourceDiffInsert(lineNumber, source);
	}

	private static SourceDiffDelete getSourceDiffDeleteFromDiffDescription(
			String[] lines, int deleteLine) {
		ArrayList<String> source = new ArrayList<String>();
		int lineNumber = StringUtil.extractIntegerFromString(lines[deleteLine]);
		for (int i = deleteLine + 1;; i++) {
			if (lines[i].startsWith(Diff.HEADER_ARROW))
				break;
			else 
				source.add(lines[i]);
		}
		return new SourceDiffDelete(lineNumber, source);
	}

}
