package animation.autoedition;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultiFileEdition {

	ArrayList<SingleFileEdition> File_Editions;
	
	public MultiFileEdition(ArrayList arr)
	{
		File_Editions = arr;
	}
	
	public void play() throws Exception
	{
		for(SingleFileEdition edition : File_Editions)
		{
			edition.adjustToSynchronizedApply();
			edition.splitEditions();
			edition.applyEditions();
			edition.getPlayingThread().join();
		}
	}
	
}
