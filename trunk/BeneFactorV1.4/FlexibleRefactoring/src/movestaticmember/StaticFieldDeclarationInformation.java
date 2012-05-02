package movestaticmember;

public class StaticFieldDeclarationInformation 
{
	public final int declaration_index;
	public final String declaration;
	public final int name_index;
	public StaticFieldDeclarationInformation (String dec, int d, int n)
	{
		declaration = dec;
		declaration_index = d;
		name_index = n;	
	}
}