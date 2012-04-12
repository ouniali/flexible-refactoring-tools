package animation.autoedition;

import java.util.ArrayList;

public class MultiFileEdition {

	ArrayList<SingleFileEdition> File_Editions;
	
	public MultiFileEdition(ArrayList arr)
	{
		File_Editions = arr;
	}
	
	public void play() throws Exception
	{
		int i = File_Editions.size();
		for(SingleFileEdition edition : File_Editions)
		{
			System.out.println(i);
			edition.adjustToSynchronizedApply();
			edition.splitEditions();
			edition.applyEditions();
		}
	}
	
}
