package Rename;
import java.util.*;
import java.util.Map.Entry;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.*;

import ASTree.ASTreeManipulationMethods;
import ASTree.NameBindingInformationVisitor;

public class SimpleNamesInCompilationUnit {
	
	CompilationUnit tree;
	NameBindingInformationVisitor visitor;
	Hashtable<String, ArrayList<SimpleName>> SimpleNameTable;
	
	// the compilation unit should be generated directly from ICompilationUnit
	public SimpleNamesInCompilationUnit(ICompilationUnit unit)
	{
		tree = ASTreeManipulationMethods.parseICompilationUnit(unit);
		visitor = new NameBindingInformationVisitor();
		tree.accept(visitor);
		SimpleNameTable = visitor.getEntireSimpleNameBindingTable();
	}
	
	public ArrayList<SimpleName> getSimpleNamesOfBindingInCompilatioUnit(String bind)
	{
		if(bind == null || bind.equals(""))
			return new ArrayList<SimpleName>();
		ArrayList<SimpleName> res = SimpleNameTable.get(bind);
		if(res == null)
			return new ArrayList<SimpleName>(); 
		else
			return res;
	}
}
