package ASTree.CUHistory;

import org.eclipse.jdt.core.dom.CompilationUnit;

import ASTree.ASTChange;

public interface CUHistoryInterface {
	public void addAST(CompilationUnit tree) throws Exception;
	public void removeOldASTs(int count) throws Exception;
	public int getRecordSize();
	public ASTChange getLatestChange();
	public String getCompilationUnitName();
	public String getPackageName();
	public CompilationUnitHistoryRecord getMostRecentRecord();
}
