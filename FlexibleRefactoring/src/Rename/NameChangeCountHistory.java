package Rename;

import java.util.*;
import org.eclipse.jdt.core.dom.*;
import ASTree.*;
//this class records the number of names of similar binding after each name change.

public class NameChangeCountHistory {
	
	ArrayList<IBinding> bindings;// the bindings
	ArrayList<Integer> currentbindingCounts;// the current number of names that binds to a binding
	ArrayList<Integer> nameChangingCounts;// the total number of name changes for a binding
	
	public NameChangeCountHistory()
	{
		bindings = new ArrayList<IBinding>();
		currentbindingCounts = new ArrayList<Integer>();
		nameChangingCounts = new ArrayList<Integer>();
	}
	//bind is the binding before name has been changed, the count is the number of names that binds identically with the given bind before name change.
	public void addNameChange(IBinding bind, int countBeforeRename)
	{
		if(bind != null)
		{
			int index = getBindingIndexInArray(bind);
			if(index == -1)
			{
				bindings.add(bind);
				currentbindingCounts.add(new Integer(countBeforeRename-1));
				nameChangingCounts.add(new Integer(1));
			}
			else
			{			
				Integer currentNameChangingCount = nameChangingCounts.get(index);
				Integer newBindingCount = new Integer(countBeforeRename-1);
				Integer newNameChangingCount = new Integer(currentNameChangingCount.intValue() + 1);
				currentbindingCounts.remove(index);
				currentbindingCounts.add(index, newBindingCount);
				nameChangingCounts.remove(index);
				nameChangingCounts.add(index, newNameChangingCount);
			}
			
		}
	}
	private int getBindingIndexInArray(IBinding bind)
	{
		int index = 0;
		for(IBinding current: bindings)
		{
			if(current.isEqualTo(bind))
				return index;
			index++;
		}
		return -1;
		
	}
	public float getNameChangeFraction(IBinding bind)
	{
		if(bind == null)
			return -1;
		int index = getBindingIndexInArray(bind);
		if(index != -1)
		{
			float changeCount = nameChangingCounts.get(index).floatValue();
			float currentBindingCount = currentbindingCounts.get(index).floatValue();
			float totalCount = changeCount + currentBindingCount;
			return changeCount/totalCount;		
		}
		else	
			return -1;			
	}
	
	


}
