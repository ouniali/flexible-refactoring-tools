package Rename;

import java.util.*;
import org.eclipse.jdt.core.dom.*;
import ASTree.*;
//this class records the number of names of similar binding after each name change.

public class NameChangeCountHistory {
	
	ArrayList<IBinding> bindings;// the bindings
	ArrayList<Integer> bindingCounts;// the total number of names that binds to a binding
	ArrayList<Integer> nameChangingCounts;// the total number of name changes for name of a binding
	
	public NameChangeCountHistory()
	{
		bindings = new ArrayList<IBinding>();
		bindingCounts = new ArrayList<Integer>();
		nameChangingCounts = new ArrayList<Integer>();
	}
	
	public void addNameChange(IBinding bind, int count)
	{
		if(bind != null)
		{
			int index = bindings.indexOf(bind);
			if(index == -1)
			{
				bindings.add(bind);
				bindingCounts.add(new Integer(count));
				nameChangingCounts.add(new Integer(1));
			}
			else
			{
				Integer currentNameChangingCount = nameChangingCounts.get(index);
				Integer newBindingCount = new Integer(count);
				Integer newNameChangingCount = new Integer(currentNameChangingCount + 1);
				bindingCounts.remove(index);
				bindingCounts.add(index, newBindingCount);
				nameChangingCounts.remove(index);
				nameChangingCounts.add(index, newNameChangingCount);
			}
			
		}
	}
	
	
	


}
