package animation;

import java.util.ArrayList;

public class Animation {
	
	ArrayList<MovableObject> mObjects = new ArrayList<MovableObject>();
	
	public void addMovableObject(MovableObject m)
	{
		mObjects.add(m);
	}
	
	public void mergeAnimation(Animation a)
	{
		mObjects.addAll(a.mObjects);
	}
	
	public void play()
	{
		for(MovableObject m : mObjects)
		{
			m.showShell();
			m.play();
		}
	}
	
	
}
