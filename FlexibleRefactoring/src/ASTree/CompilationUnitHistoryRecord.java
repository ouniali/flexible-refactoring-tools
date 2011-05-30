package ASTree;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.*;

public class CompilationUnitHistoryRecord {
	
	static final String root = "AST_FULL";
	private final long time;
	private final String Directory;
	private final String ASTFileName;
	private final String ProjectName;
	private final String PackageName;
	private final String UnitName;
	private final IJavaProject Project;
	private final ICompilationUnit unit;
	
	
	protected CompilationUnitHistoryRecord(IJavaProject proj, ICompilationUnit iu,String pro, String pac, String un, CompilationUnit u, long t ) throws IOException
	{
		Project = proj;
		unit = iu;
		ProjectName = pro;
		PackageName = pac;
		UnitName = un;
		time = t;
		ASTFileName = PackageName + "_" + UnitName + "_" + time+".java";
		Directory = root + File.separator + ProjectName;
		new File(Directory).mkdirs();
		saveAST(Directory + File.separator +ASTFileName, u);

	}
	
	
	private void saveAST(String path, CompilationUnit unit) 
	{
		try{
			File file = new File (path);
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write((unit.getRoot().toString()));
			writer.close();
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public CompilationUnit getASTree()
	{
		String path = Directory + File.separator + ASTFileName;
		String source = readAllCodeFrom(path);		
		CompilationUnit unit =  ASTreeManipulationMethods.parseSourceCode(source);
		return unit;
	}
	private String readAllCodeFrom(String path)
	{
		StringBuffer source = new StringBuffer();
		try{
		  FileInputStream fstream = new FileInputStream(path);
		  DataInputStream in = new DataInputStream(fstream);
		  BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  String strLine;
		  while ((strLine = br.readLine()) != null)
			  source.append(strLine);
		  in.close();
		  return source.toString();
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public long getTime()
	{
		return time;
	}
}
