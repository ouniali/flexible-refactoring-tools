package ASTree;

import java.util.*;

import movestaticmember.ASTChangeInformationAddStaticMember;
import movestaticmember.ASTChangeInformationDeleteStaticMember;
import movestaticmember.MoveStaticMember;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.*;

import userinterface.RefactoringMarker;

import compilation.RefactoringChances;
import extract.declaration.DecDetector;
import extract.declaration.Declaration;
import extract.declaration.LVDecDetector;
import extract.declaration.MethodDec;
import extract.declaration.MethodDecDetector;
import extract.localvariable.ELVDetector;
import extract.method.ASTEMActivity;
import extract.method.ASTEMChangeInformation;
import extract.method.EMDetector;
import flexiblerefactoring.BeneFactor;

import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.extract.localvariable.JavaRefactoringELVBase;
import JavaRefactoringAPI.extract.method.JavaRefactoringExtractMethodBase;
import Rename.ASTNameChangeInformation;
import Rename.NameChangeDetector;
import Rename.NameChangeUtil;
import Rename.NameChangeCountHistory;

public class CompilationUnitHistory {
	
	private String projectName;
	private String packageName;
	private String unitName;
	private IJavaProject project;
	private ICompilationUnit unit;
	private ArrayList<CompilationUnitHistoryRecord> records;
	
	protected CompilationUnitHistory(IJavaProject proj, ICompilationUnit u, String pro, String pac, String un)
	{
		unit = u;
		project = proj;
		projectName = pro;
		packageName = pac;
		unitName = un;
		records = new ArrayList<CompilationUnitHistoryRecord>();
	}
	
	
	protected synchronized void addAST(CompilationUnit tree) throws Exception
	{
		CompilationUnitHistoryRecord earlier = null;
		if(records.size()> 0)
			earlier = records.get(records.size()-1);
		records.add(new CompilationUnitHistoryRecord(project, unit ,
				projectName, packageName, unitName, System.currentTimeMillis(), earlier, this));
		
		detectRefactoringOpportunity(records, unit);
	}
	

	
	protected ASTChangeInformation getMostRecentASTGeneralChange()
	{
		CompilationUnitHistoryRecord newRecord = records.get(records.size()-1);
		CompilationUnitHistoryRecord oldRecord = null;
		
		if(records.size()<=1)
			return null;
		oldRecord = records.get(records.size()-2);
		ASTChangeInformation change = ASTChangeInformationGenerator.getGeneralASTChangeInformation(oldRecord, newRecord);
		return change;
	}
	protected String getCompilationUnitName()
	{
		return unitName;
	}
	
	protected String getPackageName()
	{
		return packageName;
	}
	
	public CompilationUnitHistoryRecord getMostRecentRecord()
	{
		if(records.size() > 0)
			return records.get(records.size()-1);
		else
			return null;
	}
	
	static private void detectRefactoringOpportunity(ArrayList<CompilationUnitHistoryRecord> records, ICompilationUnit unit) throws Exception
	{	
		detectRename(records, unit);
		detectEM(records, unit);
		detectMove(records, unit);
		detectELV(records, unit);
	}


	private static void detectELV(
			ArrayList<CompilationUnitHistoryRecord> records,
			ICompilationUnit unit) throws Exception {
		ELVDetector ELVDetector = new ELVDetector();
		if(ELVDetector.isELVFound(records))
		{
			RefactoringChances.getInstance().addNewRefactoringChance(ELVDetector.getELVRefactoring(unit));
			return;
		}
		if(RefactoringChances.getInstance().getPendingELVRefactoring().size() > 0)
		{
			JavaRefactoringELVBase ref = RefactoringChances.getInstance().getLatestELV();
			LVDecDetector lvd_detector = LVDecDetector.create(ref);
			if(lvd_detector.isDecDetected(records))
			{
				Declaration dec = lvd_detector.getDetectedDec();
				JavaRefactoringELVBase elv = RefactoringChances.getInstance().getLatestELV();
				dec.setRefactoring(elv);
				dec.moveRefactoring(elv);
			}
		}
	}


	private static void detectMove(
			ArrayList<CompilationUnitHistoryRecord> records,
			ICompilationUnit unit) throws Exception {
		JavaRefactoring refactoring;
		if(MoveStaticMember.LookingBackForDetectingDeleteStaticDeclarationChange(records))
		{
			System.out.println("Delete Static Declaration Detected.");
		}
		
		if(MoveStaticMember.LookingBcckForDetectingAddStaticDeclarationChange(records))
		{
			ASTChangeInformationAddStaticMember 
				addStaticChange = MoveStaticMember.getLatestAddStaticChange();
			ASTChangeInformationDeleteStaticMember 
				deleteStaticChange = MoveStaticMember.getLatestDeleteStaticChange();
			if(addStaticChange != null && deleteStaticChange != null)
			{
				if(addStaticChange.getStaticFieldDeclaration().equals(deleteStaticChange.getStaticFieldDeclaration()))
				{
					refactoring =  addStaticChange.getMoveStaticMemberRefactoring(unit, deleteStaticChange);
					RefactoringChances.getInstance().addNewRefactoringChance(refactoring);
					MoveStaticMember.clearAddStaticChange();
					MoveStaticMember.clearDeleteStaticChange();
					System.out.println("Move Static Declaration Detected.");
				}
			}
			
		}
	}


	private static void detectEM(
			ArrayList<CompilationUnitHistoryRecord> records,
			ICompilationUnit unit) throws Exception 
	{
		EMDetector EMDetector = new EMDetector();
		
		if(EMDetector.isExtractMethodDetected(records))
		{
			RefactoringChances.getInstance().addNewRefactoringChance(EMDetector.getEMRefactoring(records, unit));
			return;
		}
		
		if(!RefactoringChances.getInstance().getPendingEMRefactoring().isEmpty())
		{
			DecDetector NMSDetector = new MethodDecDetector();
			if(NMSDetector.isDecDetected(records))
			{
				JavaRefactoringExtractMethodBase EM =  
						RefactoringChances.getInstance().getLatestEM();
				MethodDec m_dec = (MethodDec) NMSDetector.getDetectedDec();
				m_dec.setRefactoring(EM);
				m_dec.moveRefactoring(EM);
			}
		}
	}


	private static void detectRename(
			ArrayList<CompilationUnitHistoryRecord> records,
			ICompilationUnit unit) throws Exception {
		JavaRefactoring refactoring;
		NameChangeDetector NCDetector = new NameChangeDetector();
		if(NCDetector.isRenameDetected(records))
		{
			refactoring = NCDetector.getRefactoring(unit);
			if(refactoring != null)
				RefactoringChances.getInstance().addNewRefactoringChance(refactoring);
		}
	}
	

	
}
