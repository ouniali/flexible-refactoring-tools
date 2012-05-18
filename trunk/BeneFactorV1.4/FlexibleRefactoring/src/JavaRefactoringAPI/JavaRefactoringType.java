package JavaRefactoringAPI;

public class JavaRefactoringType {
	
	public static final int UNCERTAIN_LOW = -1;
	
	public static final int RENAME =0;
	public static final int EXTRACT_METHOD = 1;	
	public static final int MOVE_STATIC = 2;
	public static final int EXTRACT_LOCAL_VARIABLE = 3;
	
	public static final int UNCERTAIN_HIGH = 4;
	
	public static String getRefactoringTypeName(int type)
	{
		switch(type)
		{
		case RENAME:
			return "RENAME";
		case EXTRACT_METHOD:
			return "EXTRACT_METHOD";
		default:
			return "UNCERTAIN";
		}
	}
	
	public static boolean isKnownRefactoringType(int type)
	{
		return type> UNCERTAIN_LOW && type < UNCERTAIN_HIGH;
	}

}
