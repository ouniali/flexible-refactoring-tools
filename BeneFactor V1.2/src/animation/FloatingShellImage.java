package animation;

import java.util.Calendar;
import java.util.concurrent.Semaphore;

import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import utitilies.FileUtilities;

public class FloatingShellImage extends FloatingObject
{
	Image image;
	Path path;
	
	public FloatingShellImage(int x, int y, int w, int h, Path p)
	{
		super();
		X = x;
		Y = y;
		width = w;
		height = h;
		path = p;	
	}
	
	public Shell getShell ()
	{
		return shell;
	}
	
	public synchronized void run ()
	{

		display = new Display();
		image = new Image( display, path.toOSString());
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

	

}