package Reconcile;

import java.io.IOException;
import java.util.ArrayList;


import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.ReconcileContext;
import org.eclipse.jdt.core.dom.CompilationUnit;

import ASTree.ASTree;
import ASTree.ProjectHistoryCollector;

public class RefactoringCompilerParticipant extends CompilationParticipant {
	
	private ProjectHistoryCollector collector;
	
	public RefactoringCompilerParticipant()
	{
		super();
		collector = new ProjectHistoryCollector();
		
	}
	public void buildFinished(IJavaProject project) 
	{
		System.out.println("buildFinished");
	}
	
	public void buildStarting(BuildContext[] files, boolean isBatch) 
	{
		System.out.println("buildStarting");
	}
	
	public void cleanStarting(IJavaProject project) 
	{
		System.out.println("cleanStarting");
	}
	
	public boolean isActive(IJavaProject project) 
	{
		return project.isOpen();
	}
	
	public boolean isAnnotationProcessor()
	{
		return false;
	}
	
	public void processAnnotations(BuildContext[] files) 
	{
		
	}
	
	public void reconcile(ReconcileContext context) 
	{
		
		try {
			IJavaProject pro = context.getWorkingCopy().getJavaProject();
			CompilationUnit tree = context.getAST3();
			collector.addNewProjectVersion(pro, tree);
			
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	
	

}