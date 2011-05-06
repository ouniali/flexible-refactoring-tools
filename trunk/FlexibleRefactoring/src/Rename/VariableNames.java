package Rename;
import java.util.*;

import org.eclipse.jdt.core.dom.*;

public class VariableNames {
	
	CompilationUnit tree;
	Hashtable<IBinding, ArrayList<SimpleName>> map;
	
	public VariableNames(CompilationUnit t)
	{
		tree = t;
		GetVariableNamesVisitor visitor = new GetVariableNamesVisitor();
		t.accept(visitor);
		map = visitor.getVariables();
	}
	public ArrayList<SimpleName> getTheVariablesOfBinding(IBinding bind)
	{
		return map.get(bind);
	}
}
