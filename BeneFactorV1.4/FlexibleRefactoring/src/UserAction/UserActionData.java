package UserAction;

public class UserActionData {
	
	public final static String COPY_ID = "org.eclipse.ui.edit.copy";
	public final static String CUT_ID = "org.eclipse.ui.edit.cut";
	public final static String NO_EVENT = "NO_EVENT";
	public static String Pending_Event = NO_EVENT;
	
	
	public static boolean isInterestedEvent(String id)
	{
		return id.equals(COPY_ID)||id.equals(CUT_ID);
	}
	
	synchronized public static void setPendingEvent(String id)
	{
		Pending_Event = id;
	}
	
	synchronized public static String getPendingEvent()
	{
		String temp = Pending_Event;
		Pending_Event = NO_EVENT;
		return temp;
	}
	
}
