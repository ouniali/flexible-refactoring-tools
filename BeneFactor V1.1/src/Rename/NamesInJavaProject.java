package Rename;

import java.util.*;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.*;

import ASTree.*;

public class NamesInJavaProject {
	
	IJavaProject project;
	ArrayList<NamesInCompilationUnit> UnitsNames;
	
	public NamesInJavaProject(IJavaProject pro)
	{
		try{
			project = pro;
			UnitsNames = new ArrayList<NamesInCompilationUnit>();
			ArrayList<ICompilationUnit> Units = ASTreeManipulationMethods.getICompilationUnitsOfAProject(project);	
			for(ICompilationUnit unit : Units)
				UnitsNames.add(new NamesInCompilationUnit(unit));
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}
