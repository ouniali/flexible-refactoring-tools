package JavaRefactoringAPI;

import java.util.Stack;

import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.ICompilationUnit;

import compare.SourceDiff;

import ASTree.CompilationUnitHistoryRecord;
import ASTree.CompilationUnitManipulationMethod;
import Rename.ASTNameChangeInformation;

public class JavaRefactoringRenameDiff extends JavaRefactoring {

	ASTNameChangeInformation declarationNameChange;
	String bindingKey;

	public JavaRefactoringRenameDiff(ICompilationUnit u, int l, IMarker m,
			ASTNameChangeInformation decChange) 
	{
		super(u, l, m);
		declarationNameChange = decChange;
		bindingKey = decChange.getOldNameBindingKey();
	}

	@Override
	protected void performRefactoring() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void performCodeRecovery() throws Exception {
		
		String source = declarationNameChange.getOldCompilationUnitRecord().getSourceCode();
		Stack<SourceDiff> diffs = new Stack<SourceDiff>();
		CompilationUnitHistoryRecord startRecord = declarationNameChange.getNewCompilationUnitRecord();
		CompilationUnitHistoryRecord endRecord = startRecord.getAllHistory().getMostRecentRecord();
		CompilationUnitHistoryRecord currentRecord = endRecord;

		while (currentRecord != startRecord && currentRecord != null) 
		{
			diffs.push(currentRecord.getSourceDiff());
			currentRecord = currentRecord.getPreviousRecord();
		}
		
		source = startRecord.getSourceDiff().skipChange(source);
		
		while(!diffs.isEmpty())
		{
			SourceDiff diff= diffs.pop();
			source = diff.performChange(source);
		}
	
		CompilationUnitManipulationMethod.UpdateICompilationUnit(this.getICompilationUnit(),source);

	}

	@Override
	public int getRefactoringType() {
		// TODO Auto-generated method stub
		return 0;
	}

}
