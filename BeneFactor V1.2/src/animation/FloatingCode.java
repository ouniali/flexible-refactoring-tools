package animation;

import java.util.Calendar;

import javax.swing.JFrame;

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.pushingpixels.trident.Timeline;



import utitilies.FileUtilities;
import utitilies.UserInterfaceUtilities;

public class FloatingCode extends FloatingObject{

	private MovableShellImage m_shell;
	Point destination;
	Path path;
	
	public static FloatingCode FloatingCodeFactory(ASTNode node)
	{
		return FloatingCodeFactory(node.getStartPosition(), node.getStartPosition()+ node.getLength() - 1);
	}
	
	
	public static FloatingCode FloatingCodeFactory(int start, int end)
	{
		
		JavaEditor editor = UserInterfaceUtilities.getActiveJavaEditor();
		Rectangle rect = UserInterfaceUtilities.getTextBounds(start, end, editor);
		int x = rect.x;
		int y = rect.y;
		int h = rect.height;
		int w = rect.width;
		System.out.println(x +" "+y+" "+ w+" "+h);
		if(x > 0 && y>0 && h >0 && w >0)
			return new FloatingCode(x, y, w, h);
		return null;
	}
	
	@SuppressWarnings("restriction")
	private FloatingCode(int x, int y, int w, int h)
	{		
		String p = Calendar.getInstance().getTimeInMillis() +".jpg";
		SnapShot.captureScreen(x, y, w, h, SnapShot.JPG, p);
		path = new Path(p);
		m_shell = new MovableShellImage(x, y, w, h, path);
		
	}
	
	public void MoveTo(Point d)
	{
		destination = d;		
		this.start();		
	}
	
	@Override
	public void run() {
		Timeline timeline = new Timeline(m_shell);
		timeline.addPropertyToInterpolate("X", m_shell.getX(), destination.x);
		timeline.addPropertyToInterpolate("Y", m_shell.getY(), destination.y);
		timeline.play();
	}
	
	
	public void finalize()
	{
		try {
			super.finalize();
			FileUtilities.delete(path.toOSString());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void setVisible()
	{
	
	}

	
}
