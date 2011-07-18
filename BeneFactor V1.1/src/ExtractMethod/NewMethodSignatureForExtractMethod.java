package ExtractMethod;

import java.util.StringTokenizer;

import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;

import utitilies.StringUtilities;

public class NewMethodSignatureForExtractMethod {
	
	StringTokenizer tokens;
	
	boolean modifierAvailable;
	String modifier;
	
	boolean returnTypeAvailable;
	String returnType;
	
	boolean methodNameAvailable;
	String methodName;
	
	public NewMethodSignatureForExtractMethod(String info)
	{
		tokens = new StringTokenizer(info, "()\\s", false);	

		
		modifierAvailable = false;
		parseModifier();
		
		returnTypeAvailable = false;
		parseReturnType();
		
		methodNameAvailable = false;
		parseMethodName();
	}
	
	private void parseModifier()
	{
		while(tokens.hasMoreTokens())
		{
			String token = tokens.nextToken();
		
			if(token.equals( Modifier.PRIVATE) ||
				token.equals(Modifier.PUBLIC)||
				token.equals(Modifier.PROTECTED)
			)
			{
				modifierAvailable = true;
				modifier = token;
				return;
			}
			else
				continue;
		}
	}
	
	private void parseReturnType()
	{
		while(tokens.hasMoreTokens())
		{
			String token = tokens.nextToken();
			
			if(token.equals(PrimitiveType.BOOLEAN) ||
				token.equals(PrimitiveType.BYTE) ||
				token.equals(PrimitiveType.CHAR)||
				token.equals(PrimitiveType.DOUBLE)||
				token.equals(PrimitiveType.FLOAT)||
				token.equals(PrimitiveType.INT)||
				token.equals(PrimitiveType.LONG)||
				token.equals(PrimitiveType.SHORT)||
				token.equals(PrimitiveType.VOID))
			{
				returnTypeAvailable = true;
				returnType = token;
				return;
			}
		}
	}
	
	private void parseMethodName()
	{
		if(modifierAvailable && returnTypeAvailable)
		{
			while(tokens.hasMoreTokens())
			{
				String token = tokens.nextToken();
				if(!token.equals(modifier ) && !token.equals(returnType) && StringUtilities.isJavaIdentifier(token))
				{
					methodNameAvailable = true;
					methodName = token;
					return;
				}
			}
		}
	}

}
