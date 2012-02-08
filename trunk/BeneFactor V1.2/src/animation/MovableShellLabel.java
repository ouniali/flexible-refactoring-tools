package animation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class MovableShellLabel extends Thread{
	


	int X;
	int Y;
	int width;
	int height;
	
	Shell shell;
	Display display;
	String text;
	Label label;
	
	public MovableShellLabel(int x, int y, int w, int h, String t)
	{
		X = x;
		Y = y;
		width = w;
		height = h;
		text = t;
		start();
	}
	
	public synchronized void run ()
	{
		display = new Display ();
		shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
		shell.setBounds(X, Y, width, height);
	    label = new Label (shell, SWT.CENTER);
	    label.setText (text);
	    label.setBounds (shell.getClientArea ());
	    shell.open ();
	    while (!shell.isDisposed ()) {
	    	if (!display.readAndDispatch ()) display.sleep ();
	     }
	    display.dispose ();
	}
	
	
	public static void main(String arg[])
	{
		new MovableShellLabel(100, 100, 100, 100, "LOVE");
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
	
	private void updateShell() 
	{	
		try{
		while(shell == null || display == null || label == null)
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
