package Rename;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.dom.Name;

import util.ASTUtil;



public class NamesInPackage {
	
	ArrayList<NamesInCompilationUnit> UnitsNames;
	IPackageFragment package_fragment;
	
	public NamesInPackage(IPackageFragment p) throws Exception
	{
		package_fragment = p;
		ArrayList<ICompilationUnit> units = ASTUtil.getICompilationUnitOfPackage(package_fragment);
		UnitsNames = new ArrayList<NamesInCompilationUnit>();
		for(ICompilationUnit unit: units)
			UnitsNames.add(new NamesInCompilationUnit(unit));
	}
	
	public Name getNameOfBinding(String binding)
	{
		for(NamesInCompilationUnit unit_name : UnitsNames)
		{
			if(unit_name.isBindingExisting(binding))
				return unit_name.getNamesWithBinding(binding).get(0);
		}
		return null;
	}
	
	
}
