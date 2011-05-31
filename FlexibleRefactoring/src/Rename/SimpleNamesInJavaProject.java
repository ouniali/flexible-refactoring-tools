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
		ArrayList<ICompilationUnit> Units = ASTreeManipulationMethods.getICompilationUnitsOfAProject(project);	
		for(ICompilationUnit unit : Units)
			UnitsNames.add(new SimpleNamesInCompilationUnit(ASTreeManipulationMethods.parseICompilationUnit(unit)));
	}
	public ArrayList<SimpleName> getSimpleNamesOfBindingInJavaProject(String binding)
	{
		ArrayList<SimpleName> names = new ArrayList<SimpleName>();
	
		for (SimpleNamesInCompilationUnit unitNames : UnitsNames)
		{
			ArrayList<SimpleName> NameInUnit = unitNames.getSimpleNamesOfBindingInCompilatioUnit(binding);
			if(NameInUnit != null)	
				names.addAll(NameInUnit);
		}
		
		if(names.size() == 0)
			return null;
		else
			return names;
	}
}
