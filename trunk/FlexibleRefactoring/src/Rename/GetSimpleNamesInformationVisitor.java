package Rename;

import java.util.*;

import org.eclipse.jdt.core.dom.*;

public class GetSimpleNamesInformationVisitor extends ASTVisitor {
	
	
	Hashtable<String, ArrayList<SimpleName>> Variables;
	Hashtable<String, ArrayList<SimpleName>> Methods;
	Hashtable<String, ArrayList<SimpleName>> Packages;
	Hashtable<String, ArrayList<SimpleName>> Types;
	Hashtable<String, ArrayList<SimpleName>> Annotations;
	Hashtable<String, ArrayList<SimpleName>> MemberValuePairs;
	
	public GetSimpleNamesInformationVisitor()
	{
		Variables = new Hashtable<String, ArrayList<SimpleName>>();
		Methods = new Hashtable<String, ArrayList<SimpleName>>();
		Packages = new Hashtable<String, ArrayList<SimpleName>>();
		Types = new Hashtable<String, ArrayList<SimpleName>>();
		Annotations = new Hashtable<String, ArrayList<SimpleName>>();
		MemberValuePairs = new Hashtable<String, ArrayList<SimpleName>>();
	}
	@Override
	public boolean visit(SimpleName node) {
		IBinding binding = node.resolveBinding();
		ArrayList<SimpleName> list;
		Hashtable<String, ArrayList<SimpleName>> correspondingTable;
		
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
	
		list = correspondingTable.get(binding.getKey());
		if(list != null)
		{
			list.add(node);
			return true;
		}
		list = new ArrayList<SimpleName>();
		list.add(node);
		correspondingTable.put(binding.getKey(), list);
		
		return true;
		
	}
	
	public Hashtable<String, ArrayList<SimpleName>> getVariables()
	{
		return Variables;
	}
	public Hashtable<String, ArrayList<SimpleName>> getMethods()
	{
		return Methods;
	}
	public Hashtable<String, ArrayList<SimpleName>> getAnnotations()
	{
		return Annotations;
	}
	public Hashtable<String, ArrayList<SimpleName>> getTypes()
	{
		return Types;
	}
	public Hashtable<String, ArrayList<SimpleName>> getPackages()
	{
		return Packages;
	}
	public Hashtable<String, ArrayList<SimpleName>> getMemberValuePairs()
	{
		return MemberValuePairs;
	}
	
	public Hashtable<String, ArrayList<SimpleName>> getEntireBindingTable()
	{
		Hashtable<String, ArrayList<SimpleName>> EntireTable = new Hashtable<String, ArrayList<SimpleName>>();
		EntireTable.putAll(Variables);
		EntireTable.putAll(Methods);
		EntireTable.putAll(Types);
		EntireTable.putAll(Packages);
		EntireTable.putAll(MemberValuePairs);
		EntireTable.putAll(Annotations);
		return EntireTable;
	}
}
