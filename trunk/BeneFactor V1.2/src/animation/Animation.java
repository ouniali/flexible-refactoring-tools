package animation;

import java.util.ArrayList;

public class Animation {
	
	ArrayList<MovableObject> mObjects = new ArrayList<MovableObject>();
	
	public void addMovableObject(MovableObject m)
	{
		mObjects.add(m);
	}
	
	public void play()
	{
		for(MovableObject m : mObjects)
		{
			m.showShell();
			m.start();
			while(m.isAlive())
			{
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
}
