package ASTree;
import java.io.*;

import org.eclipse.jdt.core.dom.*;

public class FileManipulationMethods {


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

	
}
