package ExtractMethod;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;

import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.extractmethod.JavaRefactoringExtractMethodBase;
import JavaRefactoringAPI.extractmethod.JavaRefactoringExtractMethodChange;

import userinterface.RefactoringMarker;
import util.StringUtil;

public class NewMethodSignature {

	int lineNumber;
	String signature;
	StringTokenizer tokens;

	boolean modifierAvailable;
	int modifier;

	boolean returnTypeAvailable;
	String returnType;

	boolean methodNameAvailable;
	String methodName;
	
	CompilationUnitHistoryRecord current_record;
	
	
	private CompilationUnitHistoryRecord getRecordNonRefactoringChangeEnd()
	{
		CompilationUnitHistoryRecord current = current_record;
		CompilationUnitHistoryRecord after = null;
		while(current != null && current.getSourceDiff() != null
				&& current.getSourceDiff().getLineNumber() == lineNumber)
		{
			after = current;
			current = current.getPreviousRecord();
		}
		if(current == null)
			return after;
		else
			return current;
		
	}

	public NewMethodSignature(String info, CompilationUnitHistoryRecord cr) {

		lineNumber = cr.getSourceDiff().getLineNumber();
		signature = info;
		signature = info.replace('(', ' ').replace(')', ' ');

		tokens = new StringTokenizer(signature);

		modifierAvailable = false;
		parseModifier();

		returnTypeAvailable = false;
		parseReturnType();

		methodNameAvailable = false;
		parseMethodName();
		
		current_record = cr;
	}

	private void setJavaRefactoringExtractMethod(JavaRefactoringExtractMethodBase em) {
		if (modifierAvailable)
			em.setModifier(modifier);
		if (methodNameAvailable)
			em.setMethodName(methodName);
	}

	private int getLineNumber() {
		return lineNumber;
	}

	private void parseModifier() {
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();

			if (token.equals("public")) {
				modifierAvailable = true;
				modifier = Modifier.PUBLIC;
				return;
			} else if (token.equals("private")) {
				modifierAvailable = true;
				modifier = Modifier.PRIVATE;
				return;
			} else if (token.equals("protected")) {
				modifierAvailable = true;
				modifier = Modifier.PROTECTED;
				return;
			} else
				continue;
		}
	}

	private void parseReturnType() {
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();

			if (token.equals("boolean")
					|| token.equals("byte")
					|| token.equals("char")
					|| token.equals("double")
					|| token.equals("float")
					|| token.equals("int")
					|| token.equals("long")
					|| token.equals("short")
					|| token.equals("void")) {
				returnTypeAvailable = true;
				returnType = token;
				return;
			}
		}
	}

	private void parseMethodName() {
		if (modifierAvailable && returnTypeAvailable) {
			while (tokens.hasMoreTokens()) {
				String token = tokens.nextToken();
				if (!token.equals(modifier) && !token.equals(returnType)
						&& StringUtil.isJavaIdentifier(token)) {
					methodNameAvailable = true;
					methodName = token;
					return;
				}
			}
		}
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("New Method Signature: " + signature + "\n");
		if (modifierAvailable)
			buffer.append("Modifer available: " + modifier + "\n");
		if (methodNameAvailable)
			buffer.append("Method name available: " + methodName + "\n");
		return buffer.toString();
	}

	public JavaRefactoringExtractMethodBase moveRefactoring(JavaRefactoringExtractMethodBase ref,
			ICompilationUnit unit) throws Exception
	{
		int line = getLineNumber();
		IMarker marker = RefactoringMarker.addRefactoringMarkerIfNo(unit, line);
		JavaRefactoringExtractMethodBase newEM = ref.moveExtractMethodRefactoring(marker, line);
		setJavaRefactoringExtractMethod(newEM);
		newEM.setNonrefactoringChangeEnd(getRecordNonRefactoringChangeEnd());
		return newEM;
		
	}
}
