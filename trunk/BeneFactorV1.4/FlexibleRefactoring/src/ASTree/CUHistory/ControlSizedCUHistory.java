package ASTree.CUHistory;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.CompilationUnit;

import ASTree.ASTChange;
import ASTree.CompilationUnitHistoryRecord;

public class ControlSizedCUHistory implements CUHistoryInterface{

	private final CUHistoryInterface internal_history;
	
	public ControlSizedCUHistory(CUHistoryInterface his)
	{
		internal_history = his;
	}

	@Override
	public void addAST(CompilationUnit tree) throws Exception {
		internal_history.addAST(tree);
	}

	@Override
	public ASTChange getLatestChange() {
		return internal_history.getLatestChange();
	}

	@Override
	public String getCompilationUnitName() {
		return internal_history.getCompilationUnitName();
	}

	@Override
	public String getPackageName() {
		return internal_history.getPackageName();
	}

	@Override
	public CompilationUnitHistoryRecord getMostRecentRecord() {
		return internal_history.getMostRecentRecord();
	}

	@Override
	public void removeOldASTs(int count) throws Exception {
		internal_history.removeOldASTs(count);
	}
	
	
	

}
