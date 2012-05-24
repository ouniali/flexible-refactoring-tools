package ASTree.visitors;

import java.util.*;
import java.util.Map.Entry;

import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.dom.*;

public class NameBindingVisitor extends ASTVisitor{
	
	
	Hashtable<String, ArrayList<Integer>> VariablesSimple;
	Hashtable<String, ArrayList<Integer>> MethodsSimple;
	Hashtable<String, ArrayList<Integer>> PackagesSimple;
	Hashtable<String, ArrayList<Integer>> TypesSimple;
	Hashtable<String, ArrayList<Integer>> AnnotationsSimple;
	Hashtable<String, ArrayList<Integer>> MemberValuePairsSimple;
	
	Hashtable<String, ArrayList<Integer>> VariablesQualified;
	Hashtable<String, ArrayList<Integer>> MethodsQualified;
	Hashtable<String, ArrayList<Integer>> PackagesQualified;
	Hashtable<String, ArrayList<Integer>> TypesQualified;
	Hashtable<String, ArrayList<Integer>> AnnotationsQualified;
	Hashtable<String, ArrayList<Integer>> MemberValuePairsQualified;
	
	int current_index;
	
	
	public NameBindingVisitor()
	{
		
		VariablesSimple = new Hashtable<String, ArrayList<Integer>>();
		MethodsSimple = new Hashtable<String, ArrayList<Integer>>();
		PackagesSimple = new Hashtable<String, ArrayList<Integer>>();
		TypesSimple = new Hashtable<String, ArrayList<Integer>>();
		AnnotationsSimple = new Hashtable<String, ArrayList<Integer>>();
		MemberValuePairsSimple = new Hashtable<String, ArrayList<Integer>>();
		
		VariablesQualified = new Hashtable<String, ArrayList<Integer>>();
		MethodsQualified = new Hashtable<String, ArrayList<Integer>>();
		PackagesQualified = new Hashtable<String, ArrayList<Integer>>();
		TypesQualified = new Hashtable<String, ArrayList<Integer>>();
		AnnotationsQualified = new Hashtable<String, ArrayList<Integer>>();
		MemberValuePairsQualified = new Hashtable<String, ArrayList<Integer>>();
		
		current_index = 0;
		
	}
	
	public void preVisit(ASTNode node)
	{
		if(node instanceof QualifiedName)
			this.handleQName((QualifiedName)node);	
		else if (node instanceof SimpleName)
			this.handleSName((SimpleName)node);
			
		current_index ++;
	}
	
	public void handleQName(QualifiedName node)
	{
		IBinding binding = node.resolveBinding();
		ArrayList<Integer> list;
		Hashtable<String, ArrayList<Integer>> correspondingTable;
			
		if(binding == null)
			return;
		
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
			list.add(current_index);
			return;
		}
		list = new ArrayList<Integer>();
		list.add(current_index);
		correspondingTable.put(binding.getKey(), list);
		return;
		
	}
	
	public void handleSName(SimpleName node) {
		IBinding binding = node.resolveBinding();
		ArrayList<Integer> list;
		Hashtable<String, ArrayList<Integer>> correspondingTable;
			
		if(binding == null)
			return;
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
			list.add(current_index);
			return;
		}
		list = new ArrayList<Integer>();
		list.add(current_index);
		correspondingTable.put(binding.getKey(), list);
		return;
		
	}
	
	public Hashtable<String, ArrayList<Integer>> getVariablesSimple()
	{
		return VariablesSimple;
	}
	public Hashtable<String, ArrayList<Integer>> getMethodsSimple()
	{
		return MethodsSimple;
	}
	public Hashtable<String, ArrayList<Integer>> getAnnotationsSimple()
	{
		return AnnotationsSimple;
	}
	public Hashtable<String, ArrayList<Integer>> getTypesSimple()
	{
		return TypesSimple;
	}
	public Hashtable<String, ArrayList<Integer>> getPackagesSimple()
	{
		return PackagesSimple;
	}
	public Hashtable<String, ArrayList<Integer>> getMemberValuePairsSimple()
	{
		return MemberValuePairsSimple;
	}
	
	public Hashtable<String, ArrayList<Integer>> getEntireSimpleNameBindingTable()
	{
		Hashtable<String, ArrayList<Integer>> EntireTable = new Hashtable<String, ArrayList<Integer>>();
		EntireTable.putAll(VariablesSimple);
		EntireTable.putAll(MethodsSimple);
		EntireTable.putAll(TypesSimple);
		EntireTable.putAll(PackagesSimple);
		EntireTable.putAll(MemberValuePairsSimple);
		EntireTable.putAll(AnnotationsSimple);
		return EntireTable;
	}
	
	public Hashtable<String, ArrayList<Integer>> getEntireQualifiedNameBindingTable()
	{
		Hashtable<String, ArrayList<Integer>> EntireTable = new Hashtable<String, ArrayList<Integer>>();
		EntireTable.putAll(VariablesQualified);
		EntireTable.putAll(MethodsQualified);
		EntireTable.putAll(TypesQualified);
		EntireTable.putAll(PackagesQualified);
		EntireTable.putAll(MemberValuePairsQualified);
		EntireTable.putAll(AnnotationsQualified);
		return EntireTable;
	}
	public Hashtable<String, ArrayList<Integer>> getEntireNameBindingTable()
	{
		Hashtable<String, ArrayList<Integer>> EntireTable = new Hashtable<String, ArrayList<Integer>>();
		Hashtable<String, ArrayList<Integer>> SimpleTable = getEntireSimpleNameBindingTable();
		Hashtable<String, ArrayList<Integer>> QualifiedTable = getEntireQualifiedNameBindingTable();
		
		EntireTable.putAll(QualifiedTable);
		EntireTable.putAll(SimpleTable);
		return EntireTable;
	}
	
	public String getBindingInformation()
	{
		StringBuffer Binding = new StringBuffer();	
		Hashtable<String, ArrayList<Integer>> sTable = getEntireSimpleNameBindingTable();
		Hashtable<String, ArrayList<Integer>> qTable = getEntireQualifiedNameBindingTable();
	
		for (Entry<String, ArrayList<Integer>> entry: sTable.entrySet())
		{
			String key = entry.getKey();
			ArrayList<Integer> indices = entry.getValue();
			
			StringBuffer sRecord = new StringBuffer();
			sRecord.append(key);
		
			for(Integer i : indices)
				sRecord.append(":" + i);
			sRecord.append("\n");
			Binding.append(sRecord);
		}
		
		for (Entry<String, ArrayList<Integer>> entry: qTable.entrySet())
		{
			StringBuffer qRecord = new StringBuffer();
			String key = entry.getKey();
			ArrayList<Integer> indices = entry.getValue();
			
			qRecord.append(key);
			for(Integer i : indices)
				qRecord.append(":" + i);
			qRecord.append("\n");
			Binding.append(qRecord);
		}
		return Binding.toString();
	}
}
