package ASTree;

import java.util.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import util.ASTUtil;

public class ProjectAnalyzer {

	private final IJavaProject project;
	private final IPackageFragment[] packages;
	
	public ProjectAnalyzer(IJavaProject p) throws CoreException
	{
		project = p;
		packages = project.getPackageFragments();
	}
	
	public int getPackageCount()
	{
		return packages.length;
	}
	
	public List<CompilationUnit> getAllTrees() throws JavaModelException
	{
		List<CompilationUnit> AllTrees = new ArrayList<CompilationUnit>();
		for (int i = 0; i<packages.length; i++)
		{
			List<CompilationUnit> TreesOfPackage = getTrees(i);
			if(TreesOfPackage!= null && TreesOfPackage.size()>0)
				AllTrees.addAll(TreesOfPackage);
		}
		return AllTrees;
	}
	
	private List<CompilationUnit> getTrees(int index) throws JavaModelException
	{
		if(index<0 || index> getPackageCount())
			return null;
	
		IPackageFragment RequestedPackage = packages[index];
		if (RequestedPackage.getKind() == IPackageFragmentRoot.K_SOURCE) 
		{
			List<CompilationUnit> TreeSet = new ArrayList<CompilationUnit>();
			for (ICompilationUnit unit : RequestedPackage.getCompilationUnits()) 
			{
				// Now create the AST for the ICompilationUnits
				CompilationUnit parse = ASTUtil.parseICompilationUnit(unit);
				TreeSet.add(parse);

			}
			return TreeSet;
		}
		else 
			return null;
		
	}

}
