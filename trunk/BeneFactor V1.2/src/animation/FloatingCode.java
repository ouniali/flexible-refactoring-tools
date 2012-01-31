package animation;

import java.util.Calendar;

import javax.swing.JFrame;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.pushingpixels.trident.Timeline;




import utitilies.FileUtilities;
import utitilies.UserInterfaceUtilities;

public class FloatingCode implements Runnable{

	private Shell shell;
	int X;
	int Y;
	int width;
	int height;
	
	String path;
	Point destination;
	
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
		ResetShell();
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
		ResetShell();
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
		ResetShell();
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
		ResetShell();
	}



	private void ResetShell()
	{
		shell.setBounds(X, Y, width, height);
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
	
	public static FloatingCode FloatingCodeFactory( int start, int end)
	{
		
		JavaEditor editor = UserInterfaceUtilities.getActiveJavaEditor();
		Point s = UserInterfaceUtilities.getEditorPointInDisplay(start, editor);
		Point e = UserInterfaceUtilities.getEditorPointInDisplay(end, editor);
		if(s == null || e == null)
			return null;
		int lh = UserInterfaceUtilities.getEditorLineHeight(end, editor);
		int x = s.x;
		int y = s.y;
		int h = e.y - s.y + lh;
		int w = e.x - s.x;
		if(x > 0 && y>0 && h >0 && w >0)
			return new FloatingCode(x, y, w, h);
		
	
		return null;
	}
	
	@SuppressWarnings("restriction")
	private FloatingCode(int x, int y, int w, int h)
	{
		X = x;
		Y = y;
		width = w;
		height = h;
		
		path = Calendar.getInstance().getTimeInMillis() +".jpg";
		SnapShot.captureScreen(X, Y, width, height, SnapShot.JPG, path);
		shell = SnapShot.showImageSWT(X, Y, width, height, path);
	}
	
	public void MoveTo(Point d)
	{
		destination = d;
		Display.getDefault().asyncExec(this);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Timeline timeline = new Timeline(this);
		timeline.addPropertyToInterpolate("X", X, destination.x);
		timeline.addPropertyToInterpolate("Y", Y, destination.y);
		timeline.play();
	}
	
}
