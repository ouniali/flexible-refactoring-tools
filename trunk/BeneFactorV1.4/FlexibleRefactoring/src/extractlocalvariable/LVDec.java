package extractlocalvariable;

import util.StringUtil;

public class LVDec extends Declaration{

	String name;
	boolean isNameAvailable = false;
	
	public LVDec (String t)
	{
		parse(t);
	}
	
	private void parse(String s)
	{
		String[] tokens = s.split(" ");
		if(tokens.length > 1 && StringUtil.isJavaIdentifier(tokens[1]))
		{
			isNameAvailable = true;
			name = tokens[1];
		}
		else
			isNameAvailable = false;
	}
	
	
	
}
