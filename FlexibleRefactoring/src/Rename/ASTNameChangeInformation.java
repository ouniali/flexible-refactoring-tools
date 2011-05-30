package Rename;
import java.util.ArrayList;


import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;


import ASTree.*;
import JavaRefactoringAPI.JavaRenameRefactoringAPI;

public class ASTNameChangeInformation extends ASTChangeInformation {

	int nameChangeType;
	String originalName;
	int originalNameBindingCount;
	String newName;
	float percentage;
	ArrayList<SimpleName> nodesWithSameBindingWithOriginalName;
	static boolean allowFinishingRenamingAutomatically = true;
	
	
	public ASTNameChangeInformation(IJavaProject pro, ICompilationUnit iunit, ASTNode r1, long t1,ASTNode r2, long t2) throws Exception {
		super(pro, iunit, r1, t1 ,r2, t2);
		// TODO Auto-generated constructor stub

		ASTNode rootOne = this.getOldRoot();
		ASTNode rootTwo = this.getNewRoot();
		nameChangeType = NameChange.DecideNameChangeType(this.getOldRoot(), this.getNewRoot());//if the change is modifying a name, get the type of such change.
		if(nameChangeType != NameChange.NOT_NAME_CHANGE)
		{
			originalName = NameChange.getOriginalNameAndNewName(rootOne, rootTwo)[0];
			newName = NameChange.getOriginalNameAndNewName(rootOne, rootTwo)[1];
			nodesWithSameBindingWithOriginalName = NameChange.getNodesWithSameBinding(rootOne, getIJavaProject(), getICompilationUnit());
			if(nodesWithSameBindingWithOriginalName == null)
				originalNameBindingCount = -1;
			else
				originalNameBindingCount = nodesWithSameBindingWithOriginalName.size();
		}
	}
	
	public int getNameChangeType()
	{
		return nameChangeType;
	}
	
	public String getNameChangeTypeDescription()
	{
		String type = NameChange.getNameChangeTypeDescription(nameChangeType);
		if(nameChangeType == NameChange.NOT_NAME_CHANGE)
			return type;
		else
			return type+" "+originalName+" "+ getNameChangePercentage() + " "+newName;
	}
	
	public IBinding getBindingOfOldName()
	{
		if(nameChangeType != NameChange.NOT_NAME_CHANGE)
		{
			SimpleName name = (SimpleName)this.getOldRoot();
			IBinding bind = name.resolveBinding();
			return bind;
		}
		
		return null;
	}	
	
	public IBinding getBindingOfNewName()
	{
		if(nameChangeType != NameChange.NOT_NAME_CHANGE)
		{
			SimpleName name = (SimpleName)this.getNewRoot();
			IBinding bind = name.resolveBinding();
			return bind;
		}
		
		return null;
	}
	
	public int getOldNameBindingCount()
	{
		return originalNameBindingCount;
	}
	
	public float getNameChangePercentage()
	{
		return percentage;
	}
	public void setNameChangePercentage(float per)
	{
		percentage = per;
		//NameChangeAutomatically();
	}
	
	public boolean NameChangeAutomatically()
	{
		SimpleName name = (SimpleName)getNewRoot();
		IBinding binding = name.resolveBinding();
		IJavaElement element = binding.getJavaElement();
		try {
			
			JavaRenameRefactoringAPI.performRefactoring(element, "newName");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return true;
	}
	
	

}
