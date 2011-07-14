package compare;

import java.util.ArrayList;

import org.eclipse.compare.internal.*;

public class JavaSourceDiff {
	
	
	static int CHANGE;
	static int INSERT;
	
	String description;
	
	public JavaSourceDiff(String e)
	{
		System.out.println(e);
	}
	
	public static ArrayList<SourceDiff> getSourceDiffs(String des)
	{
		 ArrayList<SourceDiff> diffs = new ArrayList<SourceDiff>();
		 String[] lines = des.split("\n");
		 int index;
		 
		 
		
		 
		 return diffs;
	}
	private static ArrayList<SourceDiffChange> searchForChange(String[] lines)
	{
		ArrayList<SourceDiffChange> diffs = new ArrayList<SourceDiffChange>();
		for(int i = 0; i< lines.length; i++)
		{
		
		}
		return diffs;
	}

	

}
