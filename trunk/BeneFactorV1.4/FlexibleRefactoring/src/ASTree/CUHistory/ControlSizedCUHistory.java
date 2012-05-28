package ASTree.CUHistory;

import org.eclipse.jdt.core.dom.CompilationUnit;
import ASTree.ASTChange;

public class ControlSizedCUHistory implements CUHistoryInterface{

	private final CUHistoryInterface internal_history;
	private static final int MAX_HISTORY_SIZE = 60;
	private static final int REMOVE_COUNT = 30;
	
	public ControlSizedCUHistory(CUHistoryInterface his)
	{
		internal_history = his;
	}

	@Override
	public void addAST(CompilationUnit tree) throws Exception {
		check_and_remove();
		internal_history.addAST(tree);
	}

	private void check_and_remove() throws Exception {
		if(getRecordSize() == MAX_HISTORY_SIZE)
			removeOldASTs(REMOVE_COUNT);
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

	@Override
	public int getRecordSize() {
		return internal_history.getRecordSize();
	}
	
	
	

}
