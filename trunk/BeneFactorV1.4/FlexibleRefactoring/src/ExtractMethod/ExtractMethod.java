package ExtractMethod;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.*;

import util.StringUtil;

import compare.*;

import Rename.ASTNameChangeInformation;

import ASTree.ASTChangeInformationGenerator;
import ASTree.ASTreeManipulationMethods;
import ASTree.CompilationUnitHistoryRecord;


public class ExtractMethod {

	public static final ArrayList<ASTExtractMethodChangeInformation> detectedExtractMethodChanges = new ArrayList<ASTExtractMethodChangeInformation>();
	public static final ArrayList<ASTExtractMethodActivity> detectedExtractMethodActivities = new ArrayList<ASTExtractMethodActivity>();
	public static final int MAXIMUM_LOOK_BACK_COUNT_EXTRACT_METHOD = 5;
	public static final int MAXIMUM_LOOK_BACK_COUNT_NEW_SIGNATURE = 5;

	public static NewMethodSignatureForExtractMethod getEditingNewMethodSignature(CompilationUnitHistoryRecord newRecord) 
	{	
		CompilationUnitHistoryRecord current = newRecord;
		for(int i = 0; i < MAXIMUM_LOOK_BACK_COUNT_NEW_SIGNATURE; i ++, current = current.getPreviousRecord())
		{
			if(current == null)
				break;
			String s = MethodSignaturehelper(current);
			if(!s.equals(""))
				return new NewMethodSignatureForExtractMethod (s, current);		
		}
		return null;
	}
	
	private static String MethodSignaturehelper(CompilationUnitHistoryRecord record)
	{
		SourceDiff diff = record.getSourceDiff();
		if(diff == null)
			return "";
		CompilationUnit tree = record.getASTree();
		ASTMethodDeclarationVisitor mVisitor = new ASTMethodDeclarationVisitor();
		tree.accept(mVisitor);
		int line = diff.getLineNumber();
		if(mVisitor.isInMethod(line))
			return "";
		else
			return getCode(diff);
	}
	
	private static String getCode(SourceDiff diff)
	{
		if (diff instanceof SourceDiffChange) {
			return ((SourceDiffChange) diff).getCodeAfterChange();
		}
		else if(diff instanceof SourceDiffInsert){
			return ((SourceDiffInsert) diff).getInsertedCode();
		}
		else
			return "";
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
	
	
	public static boolean LookingBackForExtractMethodActivities(ArrayList<CompilationUnitHistoryRecord> records)
	{	
		
		int lookBackCount = Math.min(records.size(),
				MAXIMUM_LOOK_BACK_COUNT_EXTRACT_METHOD);
	
		for(int i = records.size() - 1; i >= records.size() - lookBackCount; i--)
		{
			CompilationUnitHistoryRecord current = records.get(i);
			ASTExtractMethodActivity activity = new ASTExtractMethodActivity(current);
			if(ASTExtractMethodActivity.isCopyingStatements(current))
			{
				if(!detectedExtractMethodActivities.contains(activity))
				{
					detectedExtractMethodActivities.add(activity);
					return true;
				}
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
		boolean isBlockEmpty = StringUtil.isWhiteSpaceString(block_source);
		
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
