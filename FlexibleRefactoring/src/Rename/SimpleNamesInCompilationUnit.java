package Rename;
import java.util.*;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.*;

public class SimpleNamesInCompilationUnit {
	
	CompilationUnit tree;
	GetSimpleNamesInformationVisitor visitor;
	Hashtable<IBinding, ArrayList<SimpleName>> SimpleNameTable;
	
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
		ArrayList<SimpleName> results = new ArrayList<SimpleName>();
		for (Entry<IBinding, ArrayList<SimpleName>> entry: SimpleNameTable.entrySet())
		{
			if(entry.getKey().isEqualTo(bind))
				results.addAll(entry.getValue());
		}
		if(results.isEmpty())
			return null;
		else
			return results;
	}
}
