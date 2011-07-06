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
	public ArrayList<Name> getNamesOfBindingInJavaProject(String binding)
	{
		ArrayList<Name> names = new ArrayList<Name>();
	
		for (NamesInCompilationUnit unitNames : UnitsNames)
		{
			ArrayList<Name> NameInUnit = unitNames.getNamesOfBindingInCompilatioUnit(binding);
			if(NameInUnit != null)	
				names.addAll(NameInUnit);
		}
		
		if(names.size() == 0)
			return null;
		else
			return names;
	}
	
	public Hashtable<String,ArrayList<Name>> getEntireNameBindingTableInProject()
	{
		Hashtable<String,ArrayList<Name>> entireTable = new Hashtable<String,ArrayList<Name>>();
		for(NamesInCompilationUnit unitNames: UnitsNames)
		{
			Hashtable<String, ArrayList<Name>> table = unitNames.getEntireBindingTable();
			entireTable.putAll(table);
		}	
		return entireTable;
	}
	
	public ArrayList<Name> getNamesInProject()
	{
		ArrayList<Name> allNames = new ArrayList<Name>();
		for(NamesInCompilationUnit unitNames: UnitsNames)
		{
			ArrayList<Name> names = unitNames.getNamesInCompilationUnit();
			allNames.addAll(names);
		}
		return allNames;
	}
	public boolean isFullyQualifiedNameExistingInProject(String fullName)
	{
		for(NamesInCompilationUnit unitNames: UnitsNames)
		{
			if(unitNames.isFullyQuifiedNameExistingInCompilationUnit(fullName))
				return true;
		}
		return false;
	}
}
