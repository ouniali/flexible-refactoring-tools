package Rename;

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import ASTree.*;

public class SimpleNamesInJavaProject {
	IJavaProject project;
	ArrayList<SimpleNamesInCompilationUnit> UnitNames;
	ArrayList<CompilationUnit> Units;
	
	public SimpleNamesInJavaProject(IJavaProject pro) throws Exception
	{
		project = pro;
		UnitNames = new ArrayList<SimpleNamesInCompilationUnit>();
		Units = ASTreeManipulationMethods.getCompilationUnitsOfAProject(project);
		for(CompilationUnit unit : Units)
			UnitNames.add(new SimpleNamesInCompilationUnit(unit));
	}
	public ArrayList<SimpleName> getSimpleNamesOfBindingInJavaProject(IBinding bind)
	{
		ArrayList<SimpleName> names = new ArrayList<SimpleName>();
		for (SimpleNamesInCompilationUnit unit : UnitNames)
		{
			ArrayList<SimpleName> NameInUnit = unit.getSimpleNamesOfBindingInCompilatioUnit(bind);
			if(NameInUnit !=null)
				names.addAll(NameInUnit);
		}
		
		if(names.size()==0)
			return null;
		else
			return names;
	}
}
