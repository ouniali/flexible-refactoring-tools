package ASTree;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.eclipse.jdt.core.BindingKey;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.CompilationUnit;

import utitilies.FileUtilities;
import Rename.NamesInCompilationUnit;

import compare.JavaSourceDiff;
import compare.SourceDiff;

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

	private final CompilationUnitHistoryRecord previousRecord;
	private final CompilationUnitHistory history;
	private final ArrayList<SourceDiff> diffs;

	public boolean equal(Object o)
	{
		CompilationUnitHistoryRecord another = (CompilationUnitHistoryRecord) o;
		return another.ASTFileName.equals(this.ASTFileName);
	}
	
	
	protected CompilationUnitHistoryRecord(IJavaProject proj,
			ICompilationUnit iu, String pro, String pac, String un, long t,
			CompilationUnitHistoryRecord earlierVersionP, CompilationUnitHistory his) throws Exception {
		
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
		FileUtilities.save(Directory + File.separator + ASTFileName,
				iu.getSource());
		
		CompilationUnit unit = ASTreeManipulationMethods
				.parseICompilationUnit(iu);
		NameBindingInformationVisitor bVisitor = new NameBindingInformationVisitor();
		unit.accept(bVisitor);
		
		String bInfor = bVisitor.getBindingInformation();
		FileUtilities.save(Directory + File.separator
				+ BindingFileName, bInfor);
		previousRecord = earlierVersionP;

		if (previousRecord != null)
			diffs = JavaSourceDiff.getSourceDiffs(
					previousRecord.getASTFilePath(), getASTFilePath());
		else
			diffs = null;
		
		history = his;
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
		String source = getSourceCode();
		CompilationUnit unit = ASTreeManipulationMethods.parseSourceCode(source);
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

	public long getTime() {
		return time;
	}

	public String getBindingKey(int index) {
		String path = Directory + File.separator + BindingFileName;
		String key = "";
		try {
			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] strs = strLine.split(":");
				for(int i = 1; i< strs.length ; i++)
				{
					if(Integer.parseInt(strs[i]) == index)
						key = strs[0];
				}		
			}
			in.close();
			return key;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return key;
	}

	public BindingKey getDecodedBindingKey(int index) {
		String skey = getBindingKey(index);
		BindingKey key = new BindingKey (skey);
		return key;
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
			allCount += names.getNameIndicesOfBindingInCompilatioUnit(binding).size();
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
			allCount += names.getNameIndicesOfBindingInCompilatioUnit(binding).size();
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
				{
					count = strs.length - 1;
					break;
				}
			}
			in.close();
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String getASTFilePath() {
		return Directory + File.separator + ASTFileName;
	}

	public SourceDiff getSourceDiff() {
		if (diffs != null && diffs.size() > 0)
		{
			return diffs.get(0);
		}
		else
			return null;
	}
	
	public CompilationUnitHistoryRecord getPreviousRecord()
	{
		return previousRecord;
	}
	
	public CompilationUnitHistory getAllHistory()
	{
		return history;
	}
	


}
