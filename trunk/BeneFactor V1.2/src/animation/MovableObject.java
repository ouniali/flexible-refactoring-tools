package animation;

import org.eclipse.swt.graphics.Point;

public abstract class MovableObject extends Thread{

	public abstract void run();
	public abstract void showShell();
	public abstract void MoveTo(Point p);
}
