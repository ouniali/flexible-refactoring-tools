package utitilies;

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
}
