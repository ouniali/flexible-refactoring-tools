package utitilies;

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
}
