package animation;

import java.util.Calendar;
import java.util.concurrent.Semaphore;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import utitilies.FileUtilities;

public class MovableShell extends Thread
{
	
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

	int X;
	int Y;
	int width;
	int height;
	
	final String path;
	Shell shell;
	Display display;
	Image image;
	
	public MovableShell(int x, int y, int w, int h, String p)
	{
		super();
		X = x;
		Y = y;
		width = w;
		height = h;
		path =  p;	
		start();
	
	}
	
	public Shell getShell ()
	{
		return shell;
	}
	
	public void finalize()
	{
		try {
			super.finalize();
			FileUtilities.delete(path);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	
	public synchronized void run ()
	{

		display = new Display();
		image = new Image( display, path);
		shell = new Shell(display, SWT.NO_TRIM | SWT.ON_TOP);
		shell.setBackgroundImage(image);
		shell.setBounds(X, Y, width, height);		
		
		shell.open ();
		
		 while (!display.isDisposed()) 
		 {
			 if (!display.readAndDispatch ()) 
				 display.sleep();
		 }
		 display.dispose();
	}
	
	private void updateShell() 
	{	
		try{
		if(shell == null || display == null)
			Thread.sleep(100);
		}catch(Exception e){
			e.printStackTrace();
		}
		display.syncExec(new Runnable(){
		public void run() {
			shell.setBounds(X, Y, width, height);
		}
		});
	}
	
}