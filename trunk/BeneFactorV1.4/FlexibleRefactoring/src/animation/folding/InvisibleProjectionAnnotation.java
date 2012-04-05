package animation.folding;

import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

public class InvisibleProjectionAnnotation extends ProjectionAnnotation{
		
	public InvisibleProjectionAnnotation()
	{
		super();
		this.markDeleted(true);
		this.setRangeIndication(false);
		this.markCollapsed();	
	}
	public void paint(GC gc, Canvas canvas, Rectangle rect)
	{
	
	}

}
