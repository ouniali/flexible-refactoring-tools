package animation;

import java.util.Calendar;

import javax.swing.JFrame;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;


import utitilies.FileUtilities;
import utitilies.UserInterfaceUtilities;

public class MovingASTNode {

	private Shell shell;
	Point StartPoint;
	Point EndPoint;
	int X;
	int Y;
	int width;
	int height;
	
	String path;
	
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
		FileUtilities.delete(path);
	}
	

	
	public MovingASTNode(ASTNode node)
	{
		int start = node.getStartPosition();
		int end = node.getLength()+start -1;
		JavaEditor editor = UserInterfaceUtilities.getActiveJavaEditor();
		StartPoint = UserInterfaceUtilities.getEditorPointInDisplay(start, editor);
		EndPoint = UserInterfaceUtilities.getEditorPointInDisplay(end, editor);
		X = StartPoint.x;
		Y = StartPoint.y;
		width = Math.abs(EndPoint.x - StartPoint.x);
		height = Math.abs(EndPoint.y - StartPoint.y);
		path = Calendar.getInstance().getTimeInMillis() +".jpg";
		SnapShot.captureScreen(X, Y, width, height, SnapShot.JPG, path);
		shell = SnapShot.showImageSWT(X, Y, width, height, path);
	}
	
	
	
	
	

}
