package JavaRefactoringAPI.nonrefactoring;

import java.util.List;

import compare.diff_match_patch.Patch;

import ASTree.CUHistory.CompilationUnitHistoryRecord;

public interface INonRefactoringChanges {
	public void setFirstRecord(CompilationUnitHistoryRecord r);
	public void setLastRecord (CompilationUnitHistoryRecord r);
	public void addRefactoringChange(CompilationUnitHistoryRecord r);
	public void addAllRefactoringChanges(List<CompilationUnitHistoryRecord> rs);
	public List<Patch> computePatches();
}
