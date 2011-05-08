package Rename;

import java.util.*;

import org.eclipse.jdt.core.dom.*;



public class GetVariableNamesVisitor extends ASTVisitor {
	
	
	Hashtable<IBinding, ArrayList<SimpleName>> Variables;
	
	public GetVariableNamesVisitor()
	{
		Variables = new Hashtable<IBinding, ArrayList<SimpleName>>();
	}
	@Override
	public boolean visit(SimpleName node) {
		IBinding binding = node.resolveBinding();
		ArrayList<SimpleName> list;
		
		if(binding == null)
			return true;
		if(binding.getKind() == IBinding.VARIABLE)
		{
			list = Variables.get(binding);
			if(list != null)
			{
				list.add(node);
				return true;
			}
			list = new ArrayList<SimpleName>();
			list.add(node);
			Variables.put(binding, list);
		}
		
			
		return true;
		
	}
	public Hashtable<IBinding, ArrayList<SimpleName>> getVariables()
	{
		return Variables;
	}


}
