package ASTree;
import org.eclipse.jdt.core.dom.*;

public class ProjectHistoryRecord {
	public final CompilationUnit unit;
	public final long time;
	
	protected ProjectHistoryRecord(long t, CompilationUnit u)
	{
		unit = u;
		time = t;
	}
}
