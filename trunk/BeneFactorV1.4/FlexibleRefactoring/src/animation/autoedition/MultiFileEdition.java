package animation.autoedition;

import java.util.ArrayList;
import java.util.Observable;

import animation.autoedition.ui.FileChangeDecorator;

public class MultiFileEdition extends Observable{

	ArrayList<SingleFileEdition> File_Editions;
	
	public MultiFileEdition(ArrayList arr)
	{
		File_Editions = arr;
		for(SingleFileEdition se : File_Editions)
			FileChangeDecorator.addModifiedUnit(se.unit);
	}
	
	public void play() throws Exception
	{
		for(SingleFileEdition edition : File_Editions)
		{
			edition.adjustToSynchronizedApply();
			edition.splitEditions();
			edition.applyEditions();
			edition.waitFinish();
		}
		FileChangeDecorator.clearModifiedUnit();
	}
	
}
