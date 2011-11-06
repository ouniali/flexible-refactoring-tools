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
import flexiblerefactoring.BeneFactor;

import ExtractMethod.ASTExtractMethodChangeInformation;
import ExtractMethod.ExtractMethod;
import ExtractMethod.NewMethodSignatureForExtractMethod;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.JavaRefactoringExtractMethod;
import Rename.ASTNameChangeInformation;
import Rename.NameChange;
import Rename.NameChangeCountHistory;

public class CompilationUnitHistory {
	
	String ProjectName;
	String PackageName;
	String UnitName;
	IJavaProject Project;
	ICompilationUnit unit;
	ArrayList<CompilationUnitHistoryRecord> Records;
	
	protected CompilationUnitHistory(IJavaProject proj, ICompilationUnit u, String pro, String pac, String un)
	{
		unit = u;
		Project = proj;
		ProjectName = pro;
		PackageName = pac;
		UnitName = un;
		Records = new ArrayList<CompilationUnitHistoryRecord>();
	}
	
	
	protected boolean addAST(CompilationUnit tree) throws Exception
	{
		if(Records.size()>0)
		{
			CompilationUnit lastUnit = Records.get(Records.size()-1).getASTree();
			if(tree.subtreeMatch(new ASTMatcher(), lastUnit))
				return false;
		}
		CompilationUnitHistoryRecord earlier = null;
		if(Records.size()> 0)
			earlier = Records.get(Records.size()-1);
		Records.add(new CompilationUnitHistoryRecord(Project, unit ,ProjectName, PackageName, UnitName,System.currentTimeMillis(), earlier, this));
		
		detectRefactoringOpportunity(Records, unit);
		
		return true;
	}
	

	
	protected ASTChangeInformation getMostRecentASTGeneralChange()
	{
		CompilationUnitHistoryRecord newRecord = Records.get(Records.size()-1);
		CompilationUnitHistoryRecord oldRecord = null;
		
		if(Records.size()<=1)
			return null;
		oldRecord = Records.get(Records.size()-2);
		ASTChangeInformation change = ASTChangeInformationGenerator.getGeneralASTChangeInformation(oldRecord, newRecord);
		return change;
	}
	protected String getCompilationUnitName()
	{
		return UnitName;
	}
	
	protected String getPackageName()
	{
		return PackageName;
	}
	
	public CompilationUnitHistoryRecord getMostRecentRecord()
	{
		if(Records.size() > 0)
			return Records.get(Records.size()-1);
		else
			return null;
	}
	
	static private void detectRefactoringOpportunity(ArrayList<CompilationUnitHistoryRecord> records, ICompilationUnit unit) throws Exception
	{	
		JavaRefactoring refactoring;
		
		//rename
		if(NameChange.LookingBackForDetectingRenameChange(records))
		{
			ASTNameChangeInformation infor = NameChange.detectedNameChanges.get(NameChange.detectedNameChanges.size()-1);
			refactoring = infor.getRenameRefactoring(unit);
			if(refactoring != null)
			{
				RefactoringChances.addNewRefactoringChance(refactoring);
				System.out.println("Rename detected.");	
			}
		}
		
		//extract method
		if(ExtractMethod.LookingBackForDetectingExtractMethodChange(records))
		{
			ASTExtractMethodChangeInformation infor =  ExtractMethod.detectedExtractMethodChanges.get(ExtractMethod.detectedExtractMethodChanges.size()-1);
			refactoring = infor.getJavaExtractMethodRefactoring(unit);
			if(refactoring != null)
			{
				RefactoringChances.addNewRefactoringChance(refactoring);
				System.out.println("Extract method detected.");
			}
		}
		
		if(!RefactoringChances.getPendingExtractMethodRefactoring().isEmpty())
		{		
			NewMethodSignatureForExtractMethod newSig = ExtractMethod.getEditingNewMethodSignature(records.get(records.size()-1));
			if(newSig != null)
			{
				System.out.println(newSig);
				JavaRefactoringExtractMethod pendingEM = (JavaRefactoringExtractMethod) RefactoringChances.getPendingExtractMethodRefactoring().get(0);
				RefactoringChances.removeRefactoring(pendingEM);
				int line = newSig.getLineNumber();
				IMarker marker = RefactoringMarker.addRefactoringMarkerIfNo(unit, line);
				JavaRefactoringExtractMethod newEM = pendingEM.moveExtractMethodRefactoring(marker, line);
				newSig.setJavaRefactoringExtractMethod(newEM);
				newEM.setNonrefactoringChangeEnd(newSig.getRecordNonRefactoringChangeEnd());
				RefactoringChances.addNewRefactoringChance(newEM);	
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
					RefactoringChances.addNewRefactoringChance(refactoring);
					MoveStaticMember.clearAddStaticChange();
					MoveStaticMember.clearDeleteStaticChange();
					System.out.println("Move Static Declaration Detected.");
				}
			}
			
		}
	}
	

	
}
