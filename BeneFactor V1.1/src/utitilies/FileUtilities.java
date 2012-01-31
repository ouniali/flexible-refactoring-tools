package utitilies;
import java.io.*;

import org.eclipse.jdt.core.dom.*;

public class FileUtilities {


	public static void save(String path, String str) 
	{
		try{
			File file = new File (path);
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write(str);
			writer.close();
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	 public static void delete(String file) 
	 {
		String fileName = file;
	    File f = new File(fileName);
	    if (!f.exists())
	      throw new IllegalArgumentException(
	          "Delete: no such file or directory: " + fileName);
	    if (!f.canWrite())
	      throw new IllegalArgumentException("Delete: write protected: "
	          + fileName);
	    if (f.isDirectory()) {
	      String[] files = f.list();
	      if (files.length > 0)
	        throw new IllegalArgumentException(
	            "Delete: directory not empty: " + fileName);
	    }
	    boolean success = f.delete();

		    if (!success)
		      throw new IllegalArgumentException("Delete: deletion failed");
		 }


	
}
