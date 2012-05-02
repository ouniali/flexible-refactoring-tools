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



import util.FileUtil;
import util.UIUtil;

public class MovableCode extends MovableObject{

	Path path;
	
	public static MovableCode MovableCodeFactory(ASTNode node)
	{
		return MovableCodeFactory(node.getStartPosition(), node.getStartPosition()+ node.getLength() - 1);
	}

	public static MovableCode MovableCodeFactory(int start, int end)
	{
		
		JavaEditor editor = UIUtil.getActiveJavaEditor();
		Rectangle rect = UIUtil.getTextBounds(start, end, editor);
		int x = rect.x;
		int y = rect.y;
		int h = rect.height;
		int w = rect.width;
		System.out.println(x +" "+y+" "+ w+" "+h);
		if(x > 0 && y>0 && h >0 && w >0)
			return new MovableCode(x, y, w, h);
		return null;
	}
	
	@SuppressWarnings("restriction")
	private MovableCode(int x, int y, int w, int h)
	{		
		String p = Calendar.getInstance().getTimeInMillis() +".jpg";
		SnapShot.captureScreen(x, y, w, h, SnapShot.JPG, p);
		path = new Path(p);
		f_shell = new FloatingShellImage(x, y, w, h, path);
		
	}	
	public void finalize()
	{
		try {
			super.finalize();
			FileUtil.delete(path.toOSString());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
}
