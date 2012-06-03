package compare;

import java.util.ArrayList;
import java.util.List;

public class SourceDiffMulti extends SourceDiff{

	private final List<SourceDiff> diffs;
	
	public SourceDiffMulti(int l) {
		super(0);
		diffs = new ArrayList<SourceDiff>();
	}
	
	public SourceDiffMulti(List<SourceDiff> ds)
	{
		super(0);
		diffs = ds;
	}
	
	@Override
	public String performChange(String source) {
		for(SourceDiff d : diffs)
			source = d.performChange(source);
		return source;
	}

	@Override
	public String skipChange(String source) {
		return source;
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("MultiChange:\n");
		for(SourceDiff d : diffs)
			sb.append(d.toString() + "\n");
		return sb.toString();
	}
	
	public int getLineNumber()
	{
		try {
			if(diffs.size() == 1)
				return diffs.get(0).getLineNumber();
			else
				throw new Exception("No line number available.");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			return -1;
		}
	}
	
	public boolean isConvertable2Atomic()
	{
		return diffs.size() == 1;
	}
	
	public SourceDiff convert2Atomic()
	{
		return diffs.get(0);
	}

	@Override
	public boolean isAtomic() {
		return false;
	}

}
