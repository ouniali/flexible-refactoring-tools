package animation;

import org.pushingpixels.trident.Timeline;

public class HelloWorld {

	static MovableShell mshell;
	
	public static void main(String[] args) 
	{
		
		SnapShot.captureScreen(0, 0, 90, 90, SnapShot.JPG, "Hello.jpg");
		mshell = new MovableShell(10, 10, 100, 100, "Hello.jpg");
		new Thread()
		{
			public void run()
			{
				Timeline nl = new Timeline(mshell);
				nl.addPropertyToInterpolate("X", 0, 1000);
				nl.play();
			}
			
		}.start();
	
	
	}
}
