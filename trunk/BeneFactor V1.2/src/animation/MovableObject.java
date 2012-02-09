package animation;

import org.eclipse.swt.graphics.Point;
import org.pushingpixels.trident.Timeline;

public class MovableObject extends Thread{

	FloatingObject f_shell;
	Point destination;
	int duration = 4000;
	
	public void run()
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
}
