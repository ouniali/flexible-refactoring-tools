package Rename;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

public class GetSimpleNamesInformationVisitor extends ASTVisitor {
	
	
	Hashtable<IBinding, ArrayList<SimpleName>> Variables;
	Hashtable<IBinding, ArrayList<SimpleName>> Methods;
	Hashtable<IBinding, ArrayList<SimpleName>> Packages;
	Hashtable<IBinding, ArrayList<SimpleName>> Types;
	Hashtable<IBinding, ArrayList<SimpleName>> Annotations;
	Hashtable<IBinding, ArrayList<SimpleName>> MemberValuePairs;
	
	public GetSimpleNamesInformationVisitor()
	{
		Variables = new Hashtable<IBinding, ArrayList<SimpleName>>();
		Methods = new Hashtable<IBinding, ArrayList<SimpleName>>();
		Packages = new Hashtable<IBinding, ArrayList<SimpleName>>();
		Types = new Hashtable<IBinding, ArrayList<SimpleName>>();
		Annotations = new Hashtable<IBinding, ArrayList<SimpleName>>();
		MemberValuePairs = new Hashtable<IBinding, ArrayList<SimpleName>>();
	}
	@Override
	public boolean visit(SimpleName node) {
		IBinding binding = node.resolveBinding();
		ArrayList<SimpleName> list;
		Hashtable<IBinding, ArrayList<SimpleName>> correspondingTable;
		
		if(binding == null)
			return true;
		switch(binding.getKind())
		{
		case IBinding.VARIABLE:
			correspondingTable = Variables;
			break;
		case IBinding.METHOD:
			correspondingTable = Methods;
			break;
		case IBinding.PACKAGE:
			correspondingTable = Packages;
			break;
		case IBinding.TYPE:
			correspondingTable = Types;
			break;
		case IBinding.ANNOTATION:
			correspondingTable = Annotations;
			break;
		case IBinding.MEMBER_VALUE_PAIR:
			correspondingTable = MemberValuePairs;
			break;
		default:
			correspondingTable = null;
			break;
		}
	
		list = correspondingTable.get(binding);
		if(list != null)
		{
			list.add(node);
			return true;
		}
		list = new ArrayList<SimpleName>();
		list.add(node);
		correspondingTable.put(binding, list);
		
		return true;
		
	}
	
	public Hashtable<IBinding, ArrayList<SimpleName>> getVariables()
	{
		return Variables;
	}
	public Hashtable<IBinding, ArrayList<SimpleName>> getMethods()
	{
		return Methods;
	}
	public Hashtable<IBinding, ArrayList<SimpleName>> getAnnotations()
	{
		return Annotations;
	}
	public Hashtable<IBinding, ArrayList<SimpleName>> getTypes()
	{
		return Types;
	}
	public Hashtable<IBinding, ArrayList<SimpleName>> getPackages()
	{
		return Packages;
	}
	public Hashtable<IBinding, ArrayList<SimpleName>> getMemberValuePairs()
	{
		return MemberValuePairs;
	}
	
	public Hashtable<IBinding, ArrayList<SimpleName>> getEntireBindingTable()
	{
		Hashtable<IBinding, ArrayList<SimpleName>> EntireTable = new Hashtable<IBinding, ArrayList<SimpleName>>();
		EntireTable.putAll(Variables);
		EntireTable.putAll(Methods);
		EntireTable.putAll(Types);
		EntireTable.putAll(Packages);
		EntireTable.putAll(MemberValuePairs);
		EntireTable.putAll(Annotations);
		return EntireTable;
	}
}
