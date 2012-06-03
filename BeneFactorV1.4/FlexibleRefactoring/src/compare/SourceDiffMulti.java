package compare;

import java.util.ArrayList;
import java.util.List;

public class SourceDiffMulti extends SourceDiff{

	List<SourceDiff> diffs;
	
	public SourceDiffMulti(int l) {
		super(-1);
		diffs = new ArrayList<SourceDiff>();
	}
	
	
	
	public void addDiff(SourceDiff d)
	{
		diffs.add(d);
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

}
