package animation;

import org.eclipse.swt.graphics.Point;
import org.pushingpixels.trident.Timeline;

public class MovableText extends MovableObject{
	
	
	FloatingShellText m_shell;
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
		m_shell = new FloatingShellText(x, y, s);
	}
	
	public void MoveTo(Point p)
	{
		destination = p;
		this.start();
	}
	
	
	public void run()
	{
		Timeline timeline = new Timeline(m_shell);
		timeline.addPropertyToInterpolate("X", m_shell.getX(), destination.x);
		timeline.addPropertyToInterpolate("Y", m_shell.getY(), destination.y);
		timeline.play();
	}
	
	public void showShell()
	{
		m_shell.start();
	}
	
	public static void main(String arg[])
	{
		MovableText ft = FloatingTextFactory(10, 10, "LOVE");
		ft.MoveTo(new Point(300, 300));
	}
	
}
