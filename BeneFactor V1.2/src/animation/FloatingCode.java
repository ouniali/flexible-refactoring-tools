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

public class FloatingCode extends Thread{

	private MovableShell m_shell;
	Point destination;


	

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
		int h = 100;
		int w = 100;
		if(x > 0 && y>0 && h >0 && w >0)
			return new FloatingCode(x, y, w, h);
		
	
		return null;
	}
	
	@SuppressWarnings("restriction")
	private FloatingCode(int x, int y, int w, int h)
	{		
		String path = Calendar.getInstance().getTimeInMillis() +".jpg";
		SnapShot.captureScreen(x, y, w, h, SnapShot.JPG, path);
		m_shell = SnapShot.openImageSWT(x, y, w, h, path);
	}
	
	public void MoveTo(Point d)
	{
		destination = d;
		//this.start();
	}
	
	@Override
	public void run() {
		Timeline timeline = new Timeline(this);
		timeline.addPropertyToInterpolate("X", m_shell.getX(), destination.x);
		timeline.addPropertyToInterpolate("Y", m_shell.getY(), destination.y);
		timeline.play();	
	}
	
}
