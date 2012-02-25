package animation;

import org.eclipse.swt.graphics.Point;
import org.pushingpixels.trident.Timeline;

public class MovableText extends MovableObject{
	
	public static MovableText MovableTextFactory(int x, int y , String s)
	{
		if(x >= 0 && y>= 0)
			return new MovableText(x, y, s);
		else
			return null;
	}
	
	private MovableText(int x, int y, String s)
	{
		f_shell = new FloatingShellText(x, y, s);
	}

	
	public static void main(String arg[])
	{
		MovableText ft = MovableTextFactory(10, 10, "LOVE");
		ft.showShell();
		ft.setDestination(new Point(300, 300));
	}
	
}
