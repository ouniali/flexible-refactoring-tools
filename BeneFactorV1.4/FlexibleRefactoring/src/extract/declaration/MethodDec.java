package extract.declaration;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;

import compare.SourceDiff;

import ASTree.CUHistory.CompilationUnitHistoryRecord;
import JavaRefactoringAPI.JavaRefactoring;
import JavaRefactoringAPI.extract.method.JavaRefactoringExtractMethodBase;

import userinterface.RefactoringMarker;
import util.StringUtil;

public class MethodDec extends Declaration{

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
	
	public MethodDec(String info, CompilationUnitHistoryRecord cr) {

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
			if (isType(token)) {
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


	@Override
	public void setRefactoring(JavaRefactoring ref) 
	{
		JavaRefactoringExtractMethodBase emb = (JavaRefactoringExtractMethodBase) ref;
		if (modifierAvailable)
			emb.setModifier(modifier);
		if (methodNameAvailable)
			emb.setMethodName(methodName);
		emb.setNonrefactoringChangeEnd(getRecordNotEditingOn(current_record, getLineNumber()));
	}

	@Override
	public void moveRefactoring(JavaRefactoring ref) throws Exception 
	{
		JavaRefactoringExtractMethodBase emb = (JavaRefactoringExtractMethodBase) ref;
		int line = getLineNumber();
		emb.moveRefactoring(line);
	}
}
