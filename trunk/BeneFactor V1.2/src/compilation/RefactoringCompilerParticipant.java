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
import org.eclipse.swt.graphics.Point;

import animation.FloatingCode;

import flexiblerefactoring.BeneFactor;
import ASTree.ProjectHistoryCollector;
import JavaRefactoringAPI.JavaRefactoring;
import UserAction.MylynMonitor;



public class RefactoringCompilerParticipant extends CompilationParticipant {
	
	static private ProjectHistoryCollector collector = new ProjectHistoryCollector();
			
	public RefactoringCompilerParticipant()
	{
		super();
	}
	
	public boolean isActive(IJavaProject project) 
	{
		return !BeneFactor.SHUT_DOWN && project.isOpen();
	}
	
	public void reconcile(ReconcileContext context) 
	{
		
		try {
			//below is original code
			//originalCode(context);
			testingFloatingCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	private void originalCode(ReconcileContext context) throws Exception
	{
		IJavaProject pro = context.getWorkingCopy().getJavaProject();
		CompilationUnit tree = context.getAST3();
		collector.addNewProjectVersion(pro, (ICompilationUnit)tree.getJavaElement());	
	}
	
	
	boolean test = true;
	static FloatingCode fc;
	
	private void testingFloatingCode()
	{
		if(!test)
			return;
		fc = FloatingCode.FloatingCodeFactory(0, 3);
		if(fc == null)
			return;
		System.out.println("before moving");
		fc.MoveTo(new Point(300, 300));
		test = false;
	}
	
	
	
	
	
	
	
}
