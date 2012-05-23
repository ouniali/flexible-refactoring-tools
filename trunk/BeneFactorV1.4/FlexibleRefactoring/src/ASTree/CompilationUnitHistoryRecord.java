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
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.IRegion;

import util.ASTUtil;
import util.FileUtil;
import util.UIUtil;
import Rename.NamesInCompilationUnit;
import ASTree.visitors.NameBindingInformationVisitor;
import UserAction.UserActionData;

import compare.JavaSourceDiff;
import compare.SourceDiff;
import compare.SourceDiffNull;
import extract.method.EMDetector;

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
	private final int[] seletectedRegion;
	
	

	private final CompilationUnitHistoryRecord previousRecord;
	private final CompilationUnitHistory history;
	private final ArrayList<SourceDiff> diffs;	
	private final String UserAction;


	protected CompilationUnitHistoryRecord(IJavaProject proj,
			ICompilationUnit iu, String pro, String pac, String un, long t,
			CompilationUnitHistoryRecord earlierVersionP, CompilationUnitHistory his) throws Exception 
	{		
		Project = proj;
		Unit = iu;
		ProjectName = pro;
		PackageName = pac;
		UnitName = un;
		time = t;
		ASTFileName = getSouceFileName();
		BindingFileName = getBindingTableFileName();
		Directory = root + File.separator + ProjectName;
		seletectedRegion = UIUtil.getSelectedRangeInActiveEditor();
		history = his;
		previousRecord = earlierVersionP;
		saveSourceCode(iu);	
		saveBindingTable(iu, earlierVersionP);
		diffs = initializeDiffsBetweenPreviousRecord(previousRecord);
		UserAction = UserActionData.getPendingEvent();
	}


	
	private ArrayList<SourceDiff> initializeDiffsBetweenPreviousRecord(CompilationUnitHistoryRecord previousRecord)
	{
		if (previousRecord != null)
			return JavaSourceDiff.getSourceDiffs(previousRecord.getASTFilePath(), getASTFilePath());
		else
			return null;
	}
	
	private String getBindingTableFileName() {
		return PackageName + "_" + UnitName + "_" + time
				+ "_bindng.txt";
	}


	private String getSouceFileName() {
		return PackageName + "_" + UnitName + "_" + time + ".java";
	}


	private void saveBindingTable(ICompilationUnit iu,
			CompilationUnitHistoryRecord earlierVersionP) {
		
		CompilationUnit unit = ASTUtil
				.parseICompilationUnit(iu);
		NameBindingInformationVisitor bVisitor = new NameBindingInformationVisitor();
		unit.accept(bVisitor);
		
		String bInfor = bVisitor.getBindingInformation();
		FileUtil.save(getBindingTablePath(), bInfor);
	}


	private void saveSourceCode(ICompilationUnit iu) throws JavaModelException {
		new File(Directory).mkdirs();
		FileUtil.save(getASTFilePath(),
				iu.getSource());
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

	public int[] getHighlightedRegion()
	{
		return seletectedRegion;
	}
	
	public String getHighlightedText()
	{
		int[] range = getHighlightedRegion();
		return getSourceCode().substring(range[0], range[1] + 1);
	}
	
	public CompilationUnit getASTree() {
		String source = getSourceCode();
		CompilationUnit unit = ASTUtil.parseSourceCode(source);
		return unit;
	}

	public String getSourceCode() {
		String path = getASTFilePath();
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
		String path = getBindingTablePath();
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
		ArrayList<ICompilationUnit> allOtherUnits = ASTUtil
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
		ArrayList<ICompilationUnit> allUnits = ASTUtil
				.getICompilationUnitsOfAProject(Project);
		for (ICompilationUnit unit : allUnits) {
			NamesInCompilationUnit names = new NamesInCompilationUnit(unit);
			allCount += names.getNameIndicesOfBindingInCompilatioUnit(binding).size();
		}

		return allCount;
	}

	int getBindingCount(String binding) {

		String path = getBindingTablePath();
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
	
	public String getBindingTablePath()
	{
		return Directory + File.separator + BindingFileName;
	}

	public SourceDiff getSourceDiff() {
		if (diffs != null && diffs.size() > 0)
			return diffs.get(0);
		else
			return new SourceDiffNull(0);
	}
	
	public CompilationUnitHistoryRecord getPreviousRecord()
	{
		return previousRecord;
	}
	
	public CompilationUnitHistory getAllHistory()
	{
		return history;
	}

	public boolean hasCopyCommand()
	{
		return UserAction.equals(UserActionData.COPY_ID);
	}
	
	public boolean hasCutCommand()
	{
		return UserAction.equals(UserActionData.CUT_ID);
	}
	
	public int[] getSeletectedRegion() {
		return seletectedRegion;
	}


	public boolean equals(Object o)
	{
		CompilationUnitHistoryRecord another = (CompilationUnitHistoryRecord) o;
		return another.ASTFileName.equals(this.ASTFileName);
	}
	
	static public String getHistoryFilesRoot()
	{
		return root;
	}
	


}
