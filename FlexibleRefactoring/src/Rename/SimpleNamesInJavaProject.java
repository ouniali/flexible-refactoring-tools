package Rename;

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import ASTree.*;

public class SimpleNamesInJavaProject {
	
	IJavaProject project;
	ArrayList<SimpleNamesInCompilationUnit> UnitsNames;
	
	public SimpleNamesInJavaProject(IJavaProject pro) throws Exception
	{
		project = pro;
		UnitsNames = new ArrayList<SimpleNamesInCompilationUnit>();
		ArrayList<CompilationUnit> Units = ASTreeManipulationMethods.getCompilationUnitsOfAProject(project);	
		for(CompilationUnit unit : Units)
			UnitsNames.add(new SimpleNamesInCompilationUnit(unit));
	}
	public ArrayList<SimpleName> getSimpleNamesOfBindingInJavaProject(IBinding bind)
	{
		ArrayList<SimpleName> names = new ArrayList<SimpleName>();
	
		for (SimpleNamesInCompilationUnit unitNames : UnitsNames)
		{
			ArrayList<SimpleName> NameInUnit = unitNames.getSimpleNamesOfBindingInCompilatioUnit(bind);
			if(NameInUnit != null)	
				names.addAll(NameInUnit);
		}
		
		if(names.size() == 0)
			return null;
		else
			return names;
	}
}
