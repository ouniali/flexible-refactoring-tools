package utitilies;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtilities {
	
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
}
