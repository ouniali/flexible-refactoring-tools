package animation;

import org.eclipse.swt.graphics.Point;
import org.pushingpixels.trident.Timeline;

public class MovableText extends MovableObject{
	
	
	FloatingShellText f_shell;
	Point destination;
	
	public static MovableText MovableTextFactory(int x, int y , String s)
	{
		if(x >0 && y>0)
			return new MovableText(x, y, s);
		else
			return null;
	}
	
	private MovableText(int x, int y, String s)
	{
		f_shell = new FloatingShellText(x, y, s);
	}
	
	public void MoveTo(Point p)
	{
		destination = p;
		this.start();
	}
	
	
	public void run()
	{
		Timeline timeline = new Timeline(f_shell);
		timeline.addPropertyToInterpolate("X", f_shell.getX(), destination.x);
		timeline.addPropertyToInterpolate("Y", f_shell.getY(), destination.y);
		timeline.play();
	}
	
	public void showShell()
	{
		f_shell.start();
	}
	
	public static void main(String arg[])
	{
		MovableText ft = MovableTextFactory(10, 10, "LOVE");
		ft.MoveTo(new Point(300, 300));
	}
	
}
