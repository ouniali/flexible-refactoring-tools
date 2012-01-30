package animation;

import javax.swing.JFrame;

import org.eclipse.swt.widgets.Shell;
import org.pushingpixels.trident.Timeline;

public class MovingObject {
	
	int X;
	int Y;//grid of topleft
	int Width;
	int Height;
	Shell shell;
	
	public void setX(int x) {
		X = x;
		alignFramePosition();
	}

	public void setY(int y) {
		Y = y;
		alignFramePosition();
	}

	public void setWidth(int width) {
		Width = width;
		alignFramePosition();
	}

	public void setHeight(int height) {
		Height = height;
		alignFramePosition();
	}
	
	private void alignFramePosition()
	{
		shell.setBounds(X, Y, Width, Height);
	}

	public MovingObject (Shell s)
	{
		shell = s;
	
		X = s.getX();
		Y = s.getY();
		Width = s.getWidth();
		Height = s.getHeight();
	}

	public void play()
	{
		Timeline line = new Timeline (this);
		while(true)
		{
		
		line.addPropertyToInterpolate("X", 0, 1000);
		line.addPropertyToInterpolate("Y", 0, 1000);
		line.play();

		}
	}
	
}
