package compilation;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.compiler.ReconcileContext;
import org.eclipse.jdt.core.dom.CompilationUnit;
import ASTree.ProjectHistoryCollector;
import JavaRefactoringAPI.JavaRefactoring;



public class RefactoringCompilerParticipant extends CompilationParticipant {
	
	static private ProjectHistoryCollector collector = new ProjectHistoryCollector();
			
	public RefactoringCompilerParticipant()
	{
		super();
	}
	
	public boolean isActive(IJavaProject project) 
	{
		return project.isOpen();
	}
	
	public void reconcile(ReconcileContext context) 
	{
		try {
			//below is original code
			IJavaProject pro = context.getWorkingCopy().getJavaProject();
			CompilationUnit tree = context.getAST3();
			performRefactoring((ICompilationUnit)tree.getJavaElement());					
			collector.addNewProjectVersion(pro, (ICompilationUnit)tree.getJavaElement());		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
