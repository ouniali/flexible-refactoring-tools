package animation;

import org.eclipse.swt.graphics.Point;
import org.pushingpixels.trident.Timeline;

public class MovableObject extends Thread{

	FloatingObject f_shell;
	Point destination;
	
	public void run()
	{
		Timeline timeline = new Timeline(f_shell);
		timeline.addPropertyToInterpolate("X", f_shell.getX(), destination.x);
		timeline.addPropertyToInterpolate("Y", f_shell.getY(), destination.y);
		timeline.play();
	}
	public void MoveTo(Point p)
	{
		destination = p;
		start();
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
