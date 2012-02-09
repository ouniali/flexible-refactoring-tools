package animation;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class FloatingShellText extends FloatingObject{
	
	String text;
	StyledText styled;
	
	public FloatingShellText(int x, int y, String t)
	{
		X = x;
		Y = y;
		text = t;
	}
	
	public synchronized void run ()
	{
		display = new Display ();
		shell = new Shell (display, SWT.NO_TRIM | SWT.ON_TOP);
		shell.setLayout(new FillLayout());
		
		styled = new StyledText(shell, SWT.NO_BACKGROUND);
		styled.setText(text);
	    styled.setEditable(false);
	    styled.setBackground(new Color(display, 255, 255, 255));
	    Font font = new Font(shell.getDisplay(), "Times", 11, SWT.BOLD);
	    styled.setFont(font);
	    
	    Rectangle rect = styled.getTextBounds(0, text.length()-1);
	    
	    height = rect.height;
	    width = rect.width;
	    shell.setBounds(X, Y, width, height);
	    
		shell.open ();
	    while (!shell.isDisposed ()) {
	    	if (!display.readAndDispatch ()) display.sleep ();
	     }
	    display.dispose ();
	}
	

	

}
