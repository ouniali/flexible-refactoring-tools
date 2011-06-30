package ASTree;

import java.util.*;
import java.util.Map.Entry;

import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.dom.*;

public class NameBindingInformationVisitor extends ASTVisitor {
	
	
	Hashtable<String, ArrayList<SimpleName>> VariablesSimple;
	Hashtable<String, ArrayList<SimpleName>> MethodsSimple;
	Hashtable<String, ArrayList<SimpleName>> PackagesSimple;
	Hashtable<String, ArrayList<SimpleName>> TypesSimple;
	Hashtable<String, ArrayList<SimpleName>> AnnotationsSimple;
	Hashtable<String, ArrayList<SimpleName>> MemberValuePairsSimple;
	
	Hashtable<String, ArrayList<QualifiedName>> VariablesQualified;
	Hashtable<String, ArrayList<QualifiedName>> MethodsQualified;
	Hashtable<String, ArrayList<QualifiedName>> PackagesQualified;
	Hashtable<String, ArrayList<QualifiedName>> TypesQualified;
	Hashtable<String, ArrayList<QualifiedName>> AnnotationsQualified;
	Hashtable<String, ArrayList<QualifiedName>> MemberValuePairsQualified;
	
	
	public NameBindingInformationVisitor()
	{
		VariablesSimple = new Hashtable<String, ArrayList<SimpleName>>();
		MethodsSimple = new Hashtable<String, ArrayList<SimpleName>>();
		PackagesSimple = new Hashtable<String, ArrayList<SimpleName>>();
		TypesSimple = new Hashtable<String, ArrayList<SimpleName>>();
		AnnotationsSimple = new Hashtable<String, ArrayList<SimpleName>>();
		MemberValuePairsSimple = new Hashtable<String, ArrayList<SimpleName>>();
		
		VariablesQualified = new Hashtable<String, ArrayList<QualifiedName>>();
		MethodsQualified = new Hashtable<String, ArrayList<QualifiedName>>();
		PackagesQualified = new Hashtable<String, ArrayList<QualifiedName>>();
		TypesQualified = new Hashtable<String, ArrayList<QualifiedName>>();
		AnnotationsQualified = new Hashtable<String, ArrayList<QualifiedName>>();
		MemberValuePairsQualified = new Hashtable<String, ArrayList<QualifiedName>>();
	}
	@Override
	public boolean visit(QualifiedName node)
	{
		IBinding binding = node.resolveBinding();
		ArrayList<QualifiedName> list;
		Hashtable<String, ArrayList<QualifiedName>> correspondingTable;
			
		if(binding == null)
			return true;
		
		switch(binding.getKind())
		{
		case IBinding.VARIABLE:
			correspondingTable = VariablesQualified;
			break;
		case IBinding.METHOD:
			correspondingTable = MethodsQualified;
			break;
		case IBinding.PACKAGE:
			correspondingTable = PackagesQualified;
			break;
		case IBinding.TYPE:
			correspondingTable = TypesQualified;
			break;
		case IBinding.ANNOTATION:
			correspondingTable = AnnotationsQualified;
			break;
		case IBinding.MEMBER_VALUE_PAIR:
			correspondingTable = MemberValuePairsQualified;
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
		list = new ArrayList<QualifiedName>();
		list.add(node);
		correspondingTable.put(binding.getKey(), list);
		return true;
		
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
			correspondingTable = VariablesSimple;
			break;
		case IBinding.METHOD:
			correspondingTable = MethodsSimple;
			break;
		case IBinding.PACKAGE:
			correspondingTable = PackagesSimple;
			break;
		case IBinding.TYPE:
			correspondingTable = TypesSimple;
			break;
		case IBinding.ANNOTATION:
			correspondingTable = AnnotationsSimple;
			break;
		case IBinding.MEMBER_VALUE_PAIR:
			correspondingTable = MemberValuePairsSimple;
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
	
	public Hashtable<String, ArrayList<SimpleName>> getVariablesSimple()
	{
		return VariablesSimple;
	}
	public Hashtable<String, ArrayList<SimpleName>> getMethodsSimple()
	{
		return MethodsSimple;
	}
	public Hashtable<String, ArrayList<SimpleName>> getAnnotationsSimple()
	{
		return AnnotationsSimple;
	}
	public Hashtable<String, ArrayList<SimpleName>> getTypesSimple()
	{
		return TypesSimple;
	}
	public Hashtable<String, ArrayList<SimpleName>> getPackagesSimple()
	{
		return PackagesSimple;
	}
	public Hashtable<String, ArrayList<SimpleName>> getMemberValuePairsSimple()
	{
		return MemberValuePairsSimple;
	}
	
	public Hashtable<String, ArrayList<SimpleName>> getEntireSimpleNameBindingTable()
	{
		Hashtable<String, ArrayList<SimpleName>> EntireTable = new Hashtable<String, ArrayList<SimpleName>>();
		EntireTable.putAll(VariablesSimple);
		EntireTable.putAll(MethodsSimple);
		EntireTable.putAll(TypesSimple);
		EntireTable.putAll(PackagesSimple);
		EntireTable.putAll(MemberValuePairsSimple);
		EntireTable.putAll(AnnotationsSimple);
		return EntireTable;
	}
	
	public Hashtable<String, ArrayList<QualifiedName>> getEntireQualifiedNameBindingTable()
	{
		Hashtable<String, ArrayList<QualifiedName>> EntireTable = new Hashtable<String, ArrayList<QualifiedName>>();
		EntireTable.putAll(VariablesQualified);
		EntireTable.putAll(MethodsQualified);
		EntireTable.putAll(TypesQualified);
		EntireTable.putAll(PackagesQualified);
		EntireTable.putAll(MemberValuePairsQualified);
		EntireTable.putAll(AnnotationsQualified);
		return EntireTable;
	}
	public Hashtable<String, ArrayList<Name>> getEntireNameBindingTable()
	{
		Hashtable<String, ArrayList<Name>> EntireTable = new Hashtable<String, ArrayList<Name>>();
		Hashtable<String, ArrayList<SimpleName>> SimpleTable = getEntireSimpleNameBindingTable();
		Hashtable<String, ArrayList<QualifiedName>> QualifiedTable = getEntireQualifiedNameBindingTable();
		
		for(Entry<String, ArrayList<SimpleName>> entry: SimpleTable.entrySet())
		{
			String key = entry.getKey();
			ArrayList<Name> nameList = new ArrayList<Name>();
			for(SimpleName sName: entry.getValue())
				nameList.add((Name)sName);
			if(EntireTable.containsKey(key))
				EntireTable.get(key).addAll(nameList);
			else 
				EntireTable.put(key, nameList);
		}
		
		for(Entry<String, ArrayList<QualifiedName>> entry: QualifiedTable.entrySet())
		{
			String key = entry.getKey();
			ArrayList<Name> nameList = new ArrayList<Name>();
			for(QualifiedName qName: entry.getValue())
				nameList.add((Name)qName);
			if(EntireTable.containsKey(key))
				EntireTable.get(key).addAll(nameList);
			else 
				EntireTable.put(key, nameList);
		}
		
		return EntireTable;
	}
	
	public String getBindingInformation()
	{
		StringBuffer Binding = new StringBuffer();	
		Hashtable<String, ArrayList<SimpleName>> sTable = getEntireSimpleNameBindingTable();
		Hashtable<String, ArrayList<QualifiedName>> qTable = getEntireQualifiedNameBindingTable();
		String sRecord;
		String qRecord;

		for (Entry<String, ArrayList<SimpleName>> entry: sTable.entrySet())
		{
			sRecord = entry.getKey()+ ":" + entry.getValue().get(0).getFullyQualifiedName()+":"+entry.getValue().size()+"\n";
			Binding.append(sRecord);
		}
		
		for (Entry<String, ArrayList<QualifiedName>> entry: qTable.entrySet())
		{
			qRecord = entry.getKey()+ ":" + entry.getValue().get(0).getFullyQualifiedName()+":"+entry.getValue().size()+"\n";
			Binding.append(qRecord);
		}
		return Binding.toString();
	}
}
