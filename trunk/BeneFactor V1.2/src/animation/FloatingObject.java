package animation;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class FloatingObject extends Thread{

	int X;
	int Y;
	int width;
	int height;
	Display display;
	Shell shell;
	
	public void dispose()
	{
		display.dispose();
	}
	
	public void setX(int x) {
		X = x;
		updateShell();
	}


	public void setY(int y) {
		Y = y;
		updateShell();
	}


	public void setWidth(int w) {
		width = w;
		updateShell();
	}


	public void setHeight(int h) {
		height = h;
		updateShell();
	}


	
	public int getX() {
		return X;
	}


	public int getY() {
		return Y;
	}


	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}
	public Shell getShell()
	{
		return shell;
	}
	
	public Display getDisplay()
	{
		return display;
	}
	
	public void run(){}	
	
	protected void updateShell() 
	{	
		display.syncExec(new Runnable(){
		public void run() {
			shell.setBounds(X, Y, width, height);
		}
		});
	}
	
}
