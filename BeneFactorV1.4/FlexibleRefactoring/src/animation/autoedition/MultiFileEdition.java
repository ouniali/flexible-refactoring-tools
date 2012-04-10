package animation.autoedition;

import java.util.ArrayList;

public class MultiFileEdition {

	ArrayList<SingleFileEdition> File_Editions;
	
	public MultiFileEdition(ArrayList arr)
	{
		File_Editions = arr;
	}
	
	public void playAutomaticEditions() throws Exception
	{
		for(SingleFileEdition edition : File_Editions)
		{
			edition.adjustToSynchronizedApply();
			edition.splitEditions();
			edition.applyEditions();
		}
	}
	
}
