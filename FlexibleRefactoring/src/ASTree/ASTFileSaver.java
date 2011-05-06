package ASTree;
import java.io.*;

import org.eclipse.jdt.core.dom.*;

public class ASTFileSaver {

	static private final String FullASTFolderPath = "AST_full";
	static private final String ConciseASTFolderPath = "AST_concise";
	

	static public boolean saveFullAST(CompilationUnit tree) throws IOException
	{
		String filePath = getFilePath(FullASTFolderPath);
		if(filePath == null)
			return false;
		File file = new File (filePath);
		file.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		writer.write((tree.getRoot().toString()));
		writer.close();
		return true;
	}
	
	static public boolean saveConciseAST(ASTChangeInformation change) throws IOException
	{
		String filePath = getFilePath(ConciseASTFolderPath);
		if(filePath == null)
			return false;
		File file = new File(filePath);
		file.createNewFile();	
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));	
		writer.write(change.getChangeInformation());
		writer.close();
		return true;
	}
	
	static private String getFilePath(String folder)
	{
		if(!isFolderExist(folder))
			return null;
		long time = System.currentTimeMillis();
		String originalPath = folder+"/"+time;
		if(!new File(originalPath).exists())
			return originalPath+".ast";
		String modifiedPath = originalPath;
		for (int i = 1; new File(modifiedPath).exists() ;i++)	
			modifiedPath = originalPath+"_"+i;	
		return modifiedPath+".ast";
		
	}
	
	static private boolean isFolderExist(String folder)
	{
		if(new File(folder).exists())
			return true;
		return new File(folder).mkdirs();
	}
	
}
