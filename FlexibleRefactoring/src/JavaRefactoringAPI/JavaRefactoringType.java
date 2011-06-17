package JavaRefactoringAPI;

public class JavaRefactoringType {
	
	public static final int UNCERTAIN = -1;
	public static final int RENAME =0;
	public static final int EXTRACT_METHOD = 1;
	
	public static String getRefactoringTypeName(int type)
	{
		switch(type)
		{
		case RENAME:
			return "rename";
		case EXTRACT_METHOD:
			return "extract method";
		default:
			return "uncertain refactoring type";
		}
	}

}
