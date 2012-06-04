package JavaRefactoringAPI.nonrefactoring;

import java.util.ArrayList;
import java.util.List;
import ASTree.CUHistory.CompilationUnitHistoryRecord;
import compare.diff_match_patch.Patch;

public class NonRefactoringChanges implements INonRefactoringChanges{

	private CompilationUnitHistoryRecord first;
	private CompilationUnitHistoryRecord last;
	private final List<CompilationUnitHistoryRecord> refactoring_changes;
	
	public NonRefactoringChanges(){
		refactoring_changes = new ArrayList<CompilationUnitHistoryRecord>();
	}
	
	
	@Override
	public void setFirstRecord(CompilationUnitHistoryRecord r) {
		first = r;
	}

	@Override
	public void setLastRecord(CompilationUnitHistoryRecord r) {
		last = r;
	}

	@Override
	public void addRefactoringChange(CompilationUnitHistoryRecord r) {
		refactoring_changes.add(r);
	}

	@Override
	public void addAllRefactoringChanges(List<CompilationUnitHistoryRecord> rs) {
		refactoring_changes.addAll(rs);
	}

	@Override
	public List<Patch> computePatches() {
		return null;
	}
	
	

}
