package Rename;
import java.util.*;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.*;

public class SimpleNamesInCompilationUnit {
	
	CompilationUnit tree;
	GetSimpleNamesInformationVisitor visitor;
	Hashtable<String, ArrayList<SimpleName>> SimpleNameTable;
	
	public SimpleNamesInCompilationUnit(CompilationUnit t)
	{
		tree = t;
		visitor = new GetSimpleNamesInformationVisitor();
		t.accept(visitor);
		SimpleNameTable = visitor.getEntireBindingTable();
	}
	
	public ArrayList<SimpleName> getSimpleNamesOfBindingInCompilatioUnit(IBinding bind)
	{
		if(bind == null)
			return null;
		return SimpleNameTable.get(bind.getKey());
	}
}
