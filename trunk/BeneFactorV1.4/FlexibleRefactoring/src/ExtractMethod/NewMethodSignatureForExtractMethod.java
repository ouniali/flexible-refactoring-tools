package ExtractMethod;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;

import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.extractmethod.JavaRefactoringExtractMethodBase;
import JavaRefactoringAPI.extractmethod.JavaRefactoringExtractMethodChange;

import util.StringUtil;

public class NewMethodSignatureForExtractMethod {

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
	
	
	public CompilationUnitHistoryRecord getRecordNonRefactoringChangeEnd()
	{
		int sig_line = current_record.getSourceDiff().getLineNumber();
		CompilationUnitHistoryRecord current = current_record;
		CompilationUnitHistoryRecord after = null;
		while(current != null && current.getSourceDiff() != null
				&& current.getSourceDiff().getLineNumber() == sig_line)
		{
			after = current;
			current = current.getPreviousRecord();
		}
		if(current == null)
			return after;
		else
			return current;
		
	}

	public NewMethodSignatureForExtractMethod(String info, CompilationUnitHistoryRecord cr) {

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

	public void setJavaRefactoringExtractMethod(JavaRefactoringExtractMethodBase em) {
		if (modifierAvailable)
			em.setModifier(modifier);
		if (methodNameAvailable)
			em.setMethodName(methodName);
	}

	public int getLineNumber() {
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

}
