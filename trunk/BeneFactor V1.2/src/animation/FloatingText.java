package animation;

import org.eclipse.swt.graphics.Point;
import org.pushingpixels.trident.Timeline;

public class FloatingText extends Thread{
	
	
	MovableShellText m_shell;
	Point destination;
	
	public FloatingText FloatingTextFactory(int x, int y , String s)
	{
		if(x >0 && y>0)
			return new FloatingText(x, y, s);
		else
			return null;
	}
	
	private FloatingText(int x, int y, String s)
	{
		m_shell = new MovableShellText(x, y, s);
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
	
	
}
