package Reconcile;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.ReconcileContext;
import org.eclipse.jdt.core.dom.CompilationUnit;

import userinterface.RefactoringMarker;
import ASTree.CompilationUnitManipulationMethod;
import ASTree.ProjectHistoryCollector;
import JavaRefactoringAPI.JavaRefactoring;



public class RefactoringCompilerParticipant extends CompilationParticipant {
	
	static private ProjectHistoryCollector collector = new ProjectHistoryCollector();
			
	public RefactoringCompilerParticipant()
	{
		super();
		System.out.println("participant construction");	
		
	/*	IWorkbench workbench= PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getWorkbenchWindows()[0];
		IWorkbenchPage page = window.getPages()[0];
		IEditorPart part = page.getActiveEditor();
		IEditorSite site = part.getEditorSite();		
		IActionBars actionBars = site.getActionBars();*/
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
		CompilationUnit tree;
		try {
			tree = context.getAST3();
			ICompilationUnit unit = (ICompilationUnit)tree.getJavaElement();
			RefactoringMarker.createRefactoringMarker(unit, 15, 0 );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

/*		try {
			//below is original code
			IJavaProject pro = context.getWorkingCopy().getJavaProject();
			CompilationUnit tree = context.getAST3();
			performRefactoring((ICompilationUnit)tree.getJavaElement());					
			collector.addNewProjectVersion(pro, (ICompilationUnit)tree.getJavaElement());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
*/	}
	
	 synchronized void performRefactoring(ICompilationUnit unit)
	 {
		 if(!JavaRefactoring.UnhandledRefactorings.isEmpty())
		{
			JavaRefactoring refactoring = JavaRefactoring.UnhandledRefactorings.remove(0);
			JavaRefactoring.UnhandledRefactorings.clear();
			refactoring.setEnvironment(unit);
			new Thread(refactoring).start();
		}
	 }
	

	
	

}
