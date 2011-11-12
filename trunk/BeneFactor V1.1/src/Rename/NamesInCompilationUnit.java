package Rename;
import java.util.*;
import java.util.Map.Entry;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.*;

import ASTree.ASTreeManipulationMethods;
import ASTree.NameBindingInformationVisitor;

public class NamesInCompilationUnit {
	
	CompilationUnit tree;
	NameBindingInformationVisitor visitor;
	Hashtable<String, ArrayList<Integer>> NameTable;
	
	// the compilation unit should be generated directly from ICompilationUnit
	public NamesInCompilationUnit(ICompilationUnit unit)
	{
		tree = ASTreeManipulationMethods.parseICompilationUnit(unit);
		visitor = new NameBindingInformationVisitor();
		tree.accept(visitor);
		NameTable = visitor.getEntireNameBindingTable();
	}
	
	public ArrayList<Integer> getNamesOfBindingInCompilatioUnit(String bind)
	{
		if(bind == null || bind.equals(""))
			return new ArrayList<Integer>();
		ArrayList<Integer> res = NameTable.get(bind);
		if(res == null)
			return new ArrayList<Integer>(); 
		else
			return res;
	}
	
	public Hashtable<String, ArrayList<Integer>> getEntireBindingTable()
	{
		return NameTable;
	}
	
	public ArrayList<Integer> getNamesInCompilationUnit()
	{
		ArrayList<Integer> names = new ArrayList<Integer>();
		Collection<ArrayList<Integer>> allArrays = NameTable.values();
		for(ArrayList<Integer> array: allArrays)
			names.addAll(array);
		return names;
	}
	
	public boolean isNameExistingInCompilationUnit(int index)
	{
		ArrayList<Integer> names = getNamesInCompilationUnit();
		for(int name: names)
		{
			if(name == index)
				return true;
		}
		return false;
	}
}
