package animation;

import javax.swing.JFrame;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.swt.graphics.Point;

import utitilies.UserInterfaceUtilities;

public class MovingASTNode {

	private MovingObject m_object;
	Point StartPoint;
	Point EndPoint;
	
	public MovingASTNode(ASTNode node)
	{
		int start = node.getStartPosition();
		int end = node.getLength()+start -1;
		JavaEditor editor = UserInterfaceUtilities.getActiveJavaEditor();
		StartPoint = UserInterfaceUtilities.getEditorPointInDisplay(start, editor);
		EndPoint = UserInterfaceUtilities.getEditorPointInDisplay(end, editor);
		int x = StartPoint.x;
		int y = StartPoint.y;
		int w = EndPoint.x - StartPoint.x;
		int h = EndPoint.y - StartPoint.y;
		if(w < 0)
			w = -w;
		if(h < 0 )
			h = -h;
		SnapShot.captureScreen(x, y, w, h, SnapShot.JPG, "try.jpg");
		m_object = new MovingObject();
	
	}
	
	
	
	
	

}
