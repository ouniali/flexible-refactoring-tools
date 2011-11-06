package ExtractMethod;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;

import ASTree.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoringExtractMethod;

import utitilies.StringUtilities;

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
		CompilationUnitHistoryRecord tempR = current_record;
		while(tempR.getSourceDiff().getLineNumber() == sig_line)
			tempR = tempR.getPreviousRecord();
		return tempR;
		
	}

	public NewMethodSignatureForExtractMethod(int line, String info, CompilationUnitHistoryRecord cr) {

		lineNumber = line;
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

	public void setJavaRefactoringExtractMethod(JavaRefactoringExtractMethod em) {
		if (modifierAvailable)
			em.setMethodModifier(modifier);
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
						&& StringUtilities.isJavaIdentifier(token)) {
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
