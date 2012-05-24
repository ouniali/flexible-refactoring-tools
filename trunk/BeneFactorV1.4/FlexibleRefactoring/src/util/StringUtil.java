package util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	
	public static String EMPTY_STRING = "";
	
	private static String whitespace_chars =  EMPTY_STRING       /* dummy empty string for homogeneity */
            + "\\u0009" // CHARACTER TABULATION
            + "\\u000A" // LINE FEED (LF)
            + "\\u000B" // LINE TABULATION
            + "\\u000C" // FORM FEED (FF)
            + "\\u000D" // CARRIAGE RETURN (CR)
            + "\\u0020" // SPACE
            + "\\u0085" // NEXT LINE (NEL) 
            + "\\u00A0" // NO-BREAK SPACE
            + "\\u1680" // OGHAM SPACE MARK
            + "\\u180E" // MONGOLIAN VOWEL SEPARATOR
            + "\\u2000" // EN QUAD 
            + "\\u2001" // EM QUAD 
            + "\\u2002" // EN SPACE
            + "\\u2003" // EM SPACE
            + "\\u2004" // THREE-PER-EM SPACE
            + "\\u2005" // FOUR-PER-EM SPACE
            + "\\u2006" // SIX-PER-EM SPACE
            + "\\u2007" // FIGURE SPACE
            + "\\u2008" // PUNCTUATION SPACE
            + "\\u2009" // THIN SPACE
            + "\\u200A" // HAIR SPACE
            + "\\u2028" // LINE SEPARATOR
            + "\\u2029" // PARAGRAPH SEPARATOR
            + "\\u202F" // NARROW NO-BREAK SPACE
            + "\\u205F" // MEDIUM MATHEMATICAL SPACE
            + "\\u3000" // IDEOGRAPHIC SPACE
            ;        
	
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
	
	public static String[] splitBySpace(String s)
	{
		return s.split("[" + whitespace_chars + "]+");
	}

	public static boolean isSpace(String s)
	{
		return s.matches("[" + whitespace_chars + "]+");
	}
	
	public static String[] removeEmptyTokens(String[] tokens)
	{
		List l = new ArrayList<String>();
		for(String s: tokens)
		{
			if(!s.equals(EMPTY_STRING))
				l.add(s);
		}
		return (String[]) l.toArray(new String[0]);
	}
	
	
	
	
	
}
