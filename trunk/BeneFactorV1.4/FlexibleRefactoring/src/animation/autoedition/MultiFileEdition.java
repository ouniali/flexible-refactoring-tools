package animation.autoedition;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import flexiblerefactoring.BeneFactor;

import animation.autoedition.ui.FileChangeDecorator;

public class MultiFileEdition extends Observable{

	List<SingleFileEdition> File_Editions;
	
	public MultiFileEdition(List arr)
	{
		File_Editions = arr;
		for(SingleFileEdition se : File_Editions)
			FileChangeDecorator.addModifiedUnit(se.unit);
	}
	
	public void play() throws Exception
	{
		BeneFactor.shutDown();
		for(SingleFileEdition edition : File_Editions)
		{
			edition.adjustToSynchronizedApply();
			edition.splitEditions();
			edition.applyEditions();
			edition.waitFinish();
		}
		FileChangeDecorator.clearModifiedUnit();
		BeneFactor.start();
	}
	
}
