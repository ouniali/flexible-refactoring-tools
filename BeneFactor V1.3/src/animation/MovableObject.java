package animation;

import org.eclipse.swt.graphics.Point;
import org.pushingpixels.trident.Timeline;

public class MovableObject{

	FloatingObject f_shell;
	Point destination;
	int duration = 2000;
	boolean finish_play = false;
	
	
	public void play()
	{	
		try {
			while(f_shell.getShell() == null || f_shell.getDisplay() == null)
				Thread.sleep(100);		
		} catch (Exception e) {
				e.printStackTrace();
		}
		Timeline timeline = new Timeline(f_shell);
		timeline.addPropertyToInterpolate("X", f_shell.getX(), destination.x);
		timeline.addPropertyToInterpolate("Y", f_shell.getY(), destination.y);
		timeline.setDuration(duration);
		timeline.play();
	/*	while(!timeline.isDone())
		{
			try {		
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
	}
	
	public void setDestination(Point p)
	{
		destination = p;
	}
	
	
	public void showShell()
	{
		f_shell.start();
	}
	
	public void dispose()
	{
		f_shell.dispose();
	}
	
	public boolean doesPlayFinish()
	{
		return finish_play;
	}
}
