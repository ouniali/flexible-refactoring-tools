package Rename;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

public class SimpleNamesInCompilationUnit {
	
	CompilationUnit tree;
	GetSimpleNamesInformationVisitor visitor;
	
	public SimpleNamesInCompilationUnit(CompilationUnit t)
	{
		tree = t;
		visitor = new GetSimpleNamesInformationVisitor();
		t.accept(visitor);
	}
	public ArrayList<SimpleName> getSimpleNamesOfBinding(IBinding bind)
	{
		if(bind == null)
			return null;
		
		return visitor.getEntireBindingTable().get(bind);
	}
}
