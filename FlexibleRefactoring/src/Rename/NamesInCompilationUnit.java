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
	Hashtable<String, ArrayList<Name>> NameTable;
	
	// the compilation unit should be generated directly from ICompilationUnit
	public NamesInCompilationUnit(ICompilationUnit unit)
	{
		tree = ASTreeManipulationMethods.parseICompilationUnit(unit);
		visitor = new NameBindingInformationVisitor();
		tree.accept(visitor);
		NameTable = visitor.getEntireNameBindingTable();
	}
	
	public ArrayList<Name> getNamesOfBindingInCompilatioUnit(String bind)
	{
		if(bind == null || bind.equals(""))
			return new ArrayList<Name>();
		ArrayList<Name> res = NameTable.get(bind);
		if(res == null)
			return new ArrayList<Name>(); 
		else
			return res;
	}
}
