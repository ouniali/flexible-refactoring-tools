package util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	
	public static String EMPTY_STRING = "";
	
	public static boolean isWhiteSpaceString(String source)
	{
		char[] charactors = source.toCharArray();
		for(char c : charactors)
		{
			if(!Character.isWhitespace(c))
				return false;
		}
		return true;
	}
	
	public static int extractIntegerFromString(String s) {
		Matcher matcher = Pattern.compile("\\d+").matcher(s);
		matcher.find();
		return Integer.valueOf(matcher.group());
	}
	
	public static String combineStringArray(ArrayList<String> sArray)
	{
		StringBuffer buffer = new StringBuffer();
		for(String s : sArray)
		{
			buffer.append(s);
			buffer.append('\n');
		}	
		
		return buffer.toString();
	}
	
	public static boolean isJavaIdentifier(String s) {
	    if (s.length() == 0 || !Character.isJavaIdentifierStart(s.charAt(0))) {
	        return false;
	    }
	    for (int i=1; i<s.length(); i++) {
	        if (!Character.isJavaIdentifierPart(s.charAt(i))) {
	            return false;
	        }
	    }
	    return true;
	}
	
	public static String removeWhiteSpace(String s)
	{
		return s.replaceAll("\\s+", "");		 
	}
	
	public static ArrayList<String> getMatchedStrings(String s, String reg)
	{
		ArrayList<String> results = new ArrayList<String>();
		Pattern p = Pattern.compile(reg);
		Matcher matcher = p.matcher(s);
		while(matcher.find())
		{
			 String match = matcher.group();
			 results.add(match);
		}
		return results;
	}
	

	
	
	public static void main(String arg[])
	{
		String s1 = "abc";
		String s2 = "a";
		System.out.println(s1.indexOf(s2));
	}
	
	
}
