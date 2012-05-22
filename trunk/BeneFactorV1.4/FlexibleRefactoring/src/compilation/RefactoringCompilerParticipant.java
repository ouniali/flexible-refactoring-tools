package compilation;


import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.BuildContext;
import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.core.compiler.CompilationParticipant;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.compiler.ReconcileContext;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.swt.graphics.Point;


import animation.Animation;
import animation.MovableCode;
import animation.autoedition.TestingAtomaticEdition;

import flexiblerefactoring.BeneFactor;
import ASTree.ProjectHistoryCollector;
import JavaRefactoringAPI.JavaRefactoring;
import UserAction.MylynMonitor;



public class RefactoringCompilerParticipant extends CompilationParticipant {
	
	public RefactoringCompilerParticipant()
	{
		super();
	}
	
	public boolean isActive(IJavaProject project) 
	{
		return !BeneFactor.isShutDown() && project.isOpen();
	}
	
	public void reconcile(ReconcileContext context) 
	{
		
		try {
			JobQueue.getInstance().enqueue(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	
	boolean test = true;
	static MovableCode fc;
	
	private void testingFloatingCode()
	{
		if(!test)
			return;
		fc = MovableCode.MovableCodeFactory(0, 10);
		if(fc == null)
			return;
		System.out.println("before moving");
		test = false;
		fc.setDestination(new Point(900,900));
		Animation ani = new Animation();
		ani.addMovableObject(fc);
		ani.play();
		//new Animation().addMovableObject(fc);
		//FoldingUtility.testHiding();
	}
	
	private void testingAutoEdition(ReconcileContext context) throws Exception
	{
		IJavaProject pro = context.getWorkingCopy().getJavaProject();
		ICompilationUnit unit = (ICompilationUnit)context.getAST3().getJavaElement();
		TestingAtomaticEdition.test(unit);
	}
	
	
	
	
	
}
