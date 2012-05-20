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
import extractlocalvariable.ExtractLocalVariableDetector;
import flexiblerefactoring.BeneFactor;

import ExtractMethod.ASTExtractMethodActivity;
import ExtractMethod.ASTExtractMethodChangeInformation;
import ExtractMethod.ExtractMethod;
import ExtractMethod.NewMethodSignatureForExtractMethod;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.extractmethod.JavaRefactoringExtractMethodBase;
import JavaRefactoringAPI.extractmethod.JavaRefactoringExtractMethodChange;
import Rename.ASTNameChangeInformation;
import Rename.NameChangeDetected;
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
	
	
	protected void addAST(CompilationUnit tree) throws Exception
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
		JavaRefactoring refactoring;
		
		//rename
		NameChangeDetector NCDetector = new NameChangeDetector();
		if(NCDetector.isRenameDetected(records))
		{
			ASTNameChangeInformation infor = NameChangeDetected.getInstance().getLatestDetectedChange();
			refactoring = infor.getRenameRefactoring(unit);
			RefactoringChances.getInstance().addNewRefactoringChance(refactoring);
		}
		
		//extract method
		
		if(ExtractMethod.isFoundIn(records))
			RefactoringChances.getInstance().addNewRefactoringChance(ExtractMethod.getEMRefactoring(records, unit));			

		
		
		
		if(!RefactoringChances.getInstance().getPendingExtractMethodRefactoring().isEmpty())
		{		
			NewMethodSignatureForExtractMethod newSig = ExtractMethod.getEditingNewMethodSignature(records.get(records.size()-1));
			if(newSig != null)
			{
				System.out.println(newSig);
				JavaRefactoringExtractMethodBase pendingEM = (JavaRefactoringExtractMethodBase) 
						RefactoringChances.getInstance().getPendingExtractMethodRefactoring().get(0);
				RefactoringChances.getInstance().removeRefactoring(pendingEM);
				int line = newSig.getLineNumber();
				IMarker marker = RefactoringMarker.addRefactoringMarkerIfNo(unit, line);
				JavaRefactoringExtractMethodBase newEM = pendingEM.moveExtractMethodRefactoring(marker, line);
				newSig.setJavaRefactoringExtractMethod(newEM);
				newEM.setNonrefactoringChangeEnd(newSig.getRecordNonRefactoringChangeEnd());
				RefactoringChances.getInstance().addNewRefactoringChance(newEM);	
				System.out.println("Extract method continued.");
			}
		}
		
		//move static field declaration
		
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
		
		//extract local variable
		ExtractLocalVariableDetector ELVDetector = new ExtractLocalVariableDetector();
		if(ELVDetector.isELVFound(records))
			RefactoringChances.getInstance().addNewRefactoringChance(ELVDetector.getELVRefactoring(unit));
		
			
	}
	

	
}
