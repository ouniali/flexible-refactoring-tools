package animation;

import javax.swing.JFrame;

import org.pushingpixels.trident.Timeline;

public class MovingObject {
	
	int X;
	int Y;//grid of topleft
	int Width;
	int Height;
	JFrame frame;
	
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
		frame.setBounds(X, Y, Width, Height);
	}

	public MovingObject (JFrame f)
	{
		frame = f;
		X = f.getX();
		Y = f.getY();
		Width = f.getWidth();
		Height = f.getHeight();
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
