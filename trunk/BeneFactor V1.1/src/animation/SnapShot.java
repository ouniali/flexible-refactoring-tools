package animation;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class SnapShot {
	
	public static final String JPG = "jpg";
	
	
	public static void captureScreen(int x, int y, int w, int h, String format,String path) {    
        try {
            Robot robot = new Robot();
            Rectangle rect = new Rectangle();
            rect.x = x;
            rect.y = y;
            rect.height = h;
            rect.width = w;
            BufferedImage bi=robot.createScreenCapture(rect);
            ImageIO.write(bi, format, new File(path));
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    } 
	
	public static void showImage (int x, int y, int w, int h, String path)
	{
		JFrame f = new JFrame(path);
		f.getContentPane().add(new javax.swing.JLabel(new javax.swing.ImageIcon(path)));
		f.setLocation(x, y);
		f.setSize(w,h);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setUndecorated(true);
	    f.setVisible(true);
	    
	}
	
	public static void main (String[] args)
	{
		SnapShot.captureScreen(100, 100, 200, 200, SnapShot.JPG, "try.jpg");
		SnapShot.showImage(300,300,200, 200, "try.jpg");
	}







}








