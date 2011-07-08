package ASTree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.eclipse.jdt.core.BindingKey;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.*;

import Rename.NamesInCompilationUnit;
import Rename.*;

public class CompilationUnitHistoryRecord {

	static final String root = "AST_FULL";
	private final long time;
	private final String Directory;
	private final String ASTFileName;
	private final String BindingFileName;
	private final String ProjectName;
	private final String PackageName;
	private final String UnitName;
	private final IJavaProject Project;
	private final ICompilationUnit Unit;

	protected CompilationUnitHistoryRecord(IJavaProject proj,
			ICompilationUnit iu, String pro, String pac, String un, long t)
			throws Exception {
		Project = proj;
		Unit = iu;
		ProjectName = pro;
		PackageName = pac;
		UnitName = un;
		time = t;
		ASTFileName = PackageName + "_" + UnitName + "_" + time + ".java";
		BindingFileName = PackageName + "_" + UnitName + "_" + time
				+ "_bindng.txt";
		Directory = root + File.separator + ProjectName;
		new File(Directory).mkdirs();
		FileManipulationMethods.save(Directory + File.separator + ASTFileName,
				iu.getSource());
		NameBindingInformationVisitor bVisitor = new NameBindingInformationVisitor();
		CompilationUnit unit = ASTreeManipulationMethods
				.parseICompilationUnit(iu);
		unit.accept(bVisitor);
		FileManipulationMethods.save(Directory + File.separator
				+ BindingFileName, bVisitor.getBindingInformation());
	}

	public String getPackageName() {
		return PackageName;
	}

	public String getCompilationUnitName() {
		return UnitName;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public CompilationUnit getASTree() {
		String path = Directory + File.separator + ASTFileName;
		String source = readFrom(path);
		CompilationUnit unit = ASTreeManipulationMethods
				.parseSourceCode(source);
		return unit;
	}

	public String getSourceCode() {
		String path = Directory + File.separator + ASTFileName;
		StringBuffer bString = new StringBuffer();
		try {
			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				bString.append(strLine);
				bString.append('\n');
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bString.toString();
	}

	private String readFrom(String path) {
		StringBuffer source = new StringBuffer();
		try {
			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)
				source.append(strLine);
			in.close();
			return source.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public long getTime() {
		return time;
	}

	public String getBindingKey(String fullName) {
		String path = Directory + File.separator + BindingFileName;
		String key = "";
		try {
			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] strs = strLine.split(":");
				if (strs[1].equals(fullName))
					key = strs[0];
			}
			in.close();
			return key;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public BindingKey getDecodedBindingKey(String fullName)
	{
		String key = getBindingKey(fullName);
		return new BindingKey(key);
	}
	

	public IJavaProject getIJavaProject() {
		return Project;
	}

	public ICompilationUnit getICompilationUnit() {
		return Unit;
	}

	public int getNumberOfSameBindingInHistory(String binding) throws Exception {
		int allCount = 0;
		ArrayList<ICompilationUnit> allOtherUnits = ASTreeManipulationMethods
				.getSiblingsOfACompilationUnitInItsProject(Unit, Project);

		for (ICompilationUnit unit : allOtherUnits) {	
			NamesInCompilationUnit names = new NamesInCompilationUnit(unit);
			allCount += names.getNamesOfBindingInCompilatioUnit(binding)
					.size();
		}
		allCount += getBindingCount(binding);
		return allCount;
	}

	public int getNumberOfSameBindingRightNow(String binding) throws Exception {
		int allCount = 0;
		ArrayList<ICompilationUnit> allUnits = ASTreeManipulationMethods
				.getICompilationUnitsOfAProject(Project);
		for (ICompilationUnit unit : allUnits) {
			NamesInCompilationUnit names = new NamesInCompilationUnit(unit);
			allCount += names.getNamesOfBindingInCompilatioUnit(binding)
					.size();
		}

		return allCount;
	}

	int getBindingCount(String binding) {

		String path = Directory + File.separator + BindingFileName;
		int count = 0;
		try {
			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] strs = strLine.split(":");
				if (strs[0].equals(binding))
					count += Integer.parseInt(strs[2]);
			}
			in.close();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
