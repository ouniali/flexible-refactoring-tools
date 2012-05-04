package flexiblerefactoring;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;


public class BeneFactor {
	
	private static boolean SHUT_DOWN = false;
	
	public static String getIconPath(String PicFileName)
	{
		
		try{
			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
			Path path = new Path("icons/" + PicFileName);
			URL fileURL = FileLocator.find(bundle, path, null);
			return FileLocator.resolve(fileURL).getPath();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void shutDown()
	{
		SHUT_DOWN = true;
	}
	
	public static void start()
	{
		SHUT_DOWN = false;
	}
	
	public static boolean isShutDown()
	{
		return SHUT_DOWN;
	}
}
