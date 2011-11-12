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
	public ArrayList<Integer> getNamesOfBindingInJavaProject(String binding)
	{
		ArrayList<Integer> names = new ArrayList<Integer>();
	
		for (NamesInCompilationUnit unitNames : UnitsNames)
		{
			ArrayList<Integer> NameInUnit = unitNames.getNamesOfBindingInCompilatioUnit(binding);
			if(NameInUnit != null)	
				names.addAll(NameInUnit);
		}
		
		if(names.size() == 0)
			return null;
		else
			return names;
	}
	
	public Hashtable<String,ArrayList<Integer>> getEntireNameBindingTableInProject()
	{
		Hashtable<String,ArrayList<Integer>> entireTable = new Hashtable<String,ArrayList<Integer>>();
		for(NamesInCompilationUnit unitNames: UnitsNames)
		{
			Hashtable<String, ArrayList<Integer>> table = unitNames.getEntireBindingTable();
			entireTable.putAll(table);
		}	
		return entireTable;
	}
	
	public ArrayList<Integer> getNamesInProject()
	{
		ArrayList<Integer> allNames = new ArrayList<Integer>();
		for(NamesInCompilationUnit unitNames: UnitsNames)
		{
			ArrayList<Integer> names = unitNames.getNamesInCompilationUnit();
			allNames.addAll(names);
		}
		return allNames;
	}
	public boolean isNameExistingInProject(int index)
	{
		for(NamesInCompilationUnit unitNames: UnitsNames)
		{
			if(unitNames.isNameExistingInCompilationUnit(index))
				return true;
		}
		return false;
	}
}
