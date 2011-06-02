package Reconcile;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.ReconcileContext;
import org.eclipse.jdt.core.dom.CompilationUnit;
import ASTree.ProjectHistoryCollector;
import JavaRefactoringAPI.JavaRefactoring;



public class RefactoringCompilerParticipant extends CompilationParticipant {
	
	static private ProjectHistoryCollector collector = new ProjectHistoryCollector();
	
	public RefactoringCompilerParticipant()
	{
		super();
		System.out.println("participant construction");
		
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
			//below is original code
			IJavaProject pro = context.getWorkingCopy().getJavaProject();
			CompilationUnit tree = context.getAST3();
			
			if(!JavaRefactoring.UnhandledRefactorings.isEmpty())
			{
				for(JavaRefactoring refactoring : JavaRefactoring.UnhandledRefactorings)
				{
					refactoring.setEnvironment((ICompilationUnit)tree.getJavaElement());
					refactoring.start();
				}
			}
			JavaRefactoring.UnhandledRefactorings.clear();
			collector.addNewProjectVersion(pro, tree);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

	
	

}
