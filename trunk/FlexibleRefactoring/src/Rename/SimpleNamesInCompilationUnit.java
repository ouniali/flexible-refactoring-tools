package Rename;
import java.util.*;

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
	public ArrayList<SimpleName> getSimpleNamesOfBinding(IBinding bind)
	{
		if(bind == null)
			return null;
		
		return SimpleNameTable.get(bind);
	}
}
