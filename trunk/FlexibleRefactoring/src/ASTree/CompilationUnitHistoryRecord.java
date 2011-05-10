package ASTree;
import org.eclipse.jdt.core.dom.*;

public class CompilationUnitHistoryRecord {
	public final CompilationUnit unit;
	public final long time;
	
	protected CompilationUnitHistoryRecord(long t, CompilationUnit u)
	{
		unit = u;
		time = t;
	}
}
