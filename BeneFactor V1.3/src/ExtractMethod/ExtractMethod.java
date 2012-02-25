package ExtractMethod;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.*;

import utitilies.StringUtilities;

import compare.*;

import Rename.ASTNameChangeInformation;

import ASTree.ASTChangeInformationGenerator;
import ASTree.ASTreeManipulationMethods;
import ASTree.CompilationUnitHistoryRecord;

public class ExtractMethod {

	public static ArrayList<ASTExtractMethodChangeInformation> detectedExtractMethodChanges = new ArrayList<ASTExtractMethodChangeInformation>();
	public static final int MAXIMUM_LOOK_BACK_COUNT_EXTRACT_METHOD = 5;

	public static NewMethodSignatureForExtractMethod getEditingNewMethodSignature(CompilationUnitHistoryRecord newRecord) 
	{
		SourceDiff diff = newRecord.getSourceDiff();
		ASTMethodDeclarationVisitor methodVisitor = new ASTMethodDeclarationVisitor();
		CompilationUnit tree = newRecord.getASTree();
		tree.accept(methodVisitor);

		if (diff instanceof SourceDiffChange) {
			SourceDiffChange diffChange = (SourceDiffChange) diff;
			int line = diffChange.getLineNumber();
			if( methodVisitor.getOutsideMethodDeclarationName(line).equals(""))
				return new NewMethodSignatureForExtractMethod(line, diffChange.getCodeAfterChange(), newRecord);
			else
				return null;
		} else if (diff instanceof SourceDiffInsert) {
			SourceDiffInsert diffInsert = (SourceDiffInsert) diff;
			int line = diffInsert.getLineNumber();
			if( methodVisitor.getOutsideMethodDeclarationName(line).equals(""))
				return new NewMethodSignatureForExtractMethod (line, diffInsert.getInsertedCode(), newRecord);
			else
				return null;
		} else
			return null;
	}

	public static boolean LookingBackForDetectingExtractMethodChange(ArrayList<CompilationUnitHistoryRecord> Records) 
	{
		if (Records.size() == 0)
			return false;
		CompilationUnitHistoryRecord RecordTwo = Records
				.get(Records.size() - 1);

		if (Records.size() <= 1)
			return false;

		int lookBackCount = Math.min(Records.size() - 1,
				MAXIMUM_LOOK_BACK_COUNT_EXTRACT_METHOD);
		CompilationUnitHistoryRecord RecordOne;

		for (int i = 1; i <= lookBackCount; i++) {
			int index = Records.size() - 1 - i;
			RecordOne = Records.get(index);
			ASTExtractMethodChangeInformation change = ASTChangeInformationGenerator
					.getExtractMethodASTChangeInformation(RecordOne, RecordTwo);
			if (change != null) {
				detectedExtractMethodChanges.add(change);
				return true;
			}
		}
		return false;
	}

	public static boolean isExtractMethodChange(
			CompilationUnitHistoryRecord oldRecord, ASTNode nodeOne, 
			CompilationUnitHistoryRecord newRecord, ASTNode nodeTwo) {
		if (nodeOne.getNodeType() != ASTNode.BLOCK)
			return false;

		if (nodeTwo.getNodeType() != ASTNode.BLOCK)
			return false;

		int LastIndexFromStart = getLengthOfCommonnSubnodesFromStart(nodeOne,
				nodeTwo);
		int FirstIndexFromEnd = getLengthOfCommonnSubnodesFromEnd(nodeOne,
				nodeTwo);
		int childrenOneSize = ASTreeManipulationMethods.getChildNodes(nodeOne)
				.size();
		int childrenTwoSize = ASTreeManipulationMethods.getChildNodes(nodeTwo)
				.size();

		if(childrenOneSize == 0)
			return false;
		
		String new_source = newRecord.getSourceCode();
		int begin = nodeTwo.getStartPosition();
		int end = begin + nodeTwo.getLength() - 1;
		String block_source = new_source.substring(begin, end).replace('{', ' ').replace('}', ' ');
		boolean isBlockEmpty = StringUtilities.isWhiteSpaceString(block_source);
		
		if (childrenTwoSize == 0 && !isBlockEmpty)
			return false;

		if (childrenOneSize > childrenTwoSize
				&& LastIndexFromStart + FirstIndexFromEnd >= childrenTwoSize)
			return true;
		else
			return false;
	}

	public static int getLengthOfCommonnSubnodesFromStart(ASTNode nodeOne,
			ASTNode nodeTwo) {
		int index = -1;
		ArrayList<ASTNode> childrenOne = ASTreeManipulationMethods
				.getChildNodes(nodeOne);
		ArrayList<ASTNode> childrenTwo = ASTreeManipulationMethods
				.getChildNodes(nodeTwo);
		int size = Math.min(childrenOne.size(), childrenTwo.size());
		ASTNode childOne;
		ASTNode childTwo;

		for (int i = 0; i < size; i++) {
			childOne = childrenOne.get(i);
			childTwo = childrenTwo.get(i);
			if (childOne.subtreeMatch(new ASTMatcher(), childTwo))
				index = i;
			else
				break;
		}
		return index + 1;
	}

	public static int getLengthOfCommonnSubnodesFromEnd(ASTNode nodeOne,
			ASTNode nodeTwo) {
		ArrayList<ASTNode> childrenOne = ASTreeManipulationMethods
				.getChildNodes(nodeOne);
		ArrayList<ASTNode> childrenTwo = ASTreeManipulationMethods
				.getChildNodes(nodeTwo);
		int sizeOne = childrenOne.size();
		int sizeTwo = childrenTwo.size();
		int commonSize = Math.min(sizeOne, sizeTwo);
		ASTNode childOne;
		ASTNode childTwo;
		int index = -1;

		for (int i = 0; i < commonSize; i++) {
			childOne = childrenOne.get(sizeOne - i - 1);
			childTwo = childrenTwo.get(sizeTwo - i - 1);
			if (childOne.subtreeMatch(new ASTMatcher(), childTwo))
				index = i;
			else
				break;
		}

		return index + 1;
	}
}
