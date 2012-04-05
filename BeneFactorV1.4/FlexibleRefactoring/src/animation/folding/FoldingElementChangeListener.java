package animation.folding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.ui.texteditor.ITextEditor;

public class FoldingElementChangeListener  implements IElementChangedListener{

	ITextEditor editor;
	ProjectionViewer viewer;



	ProjectionAnnotationModel model;
	ArrayList<Position> positions;
	
	public FoldingElementChangeListener(ITextEditor e, ProjectionViewer v) 
	{		
		editor = e;
		viewer = v;
		positions= new ArrayList<Position>();
	}

	public ITextEditor getEditor()
	{
		return editor;
	}
	
	public ProjectionViewer getViewer() {
		return viewer;
	}
	
	
	public void setHidingArea(int start, int length)
	{
		Position np = new Position(start, length);
		for(int i = 0; i<positions.size(); i ++)
		{
			Position op = positions.get(i);
			if(op.overlapsWith(start, length))
			{
				op.setLength(length);
				op.setOffset(start);
				elementChanged(null);
				return;
			}
		}
		positions.add(np);
		elementChanged(null);	
	}
	
	
	@Override
	public void elementChanged(ElementChangedEvent event) {
	
		//System.out.println("change");
		model = viewer.getProjectionAnnotationModel();
		
		if(model == null)
			return;
		//System.out.println("Model available");
		
		HashMap<InvisibleProjectionAnnotation, Position> newAnnotations = 
				new HashMap<InvisibleProjectionAnnotation, Position>();
		for(int i = 0; i < positions.size(); i++)
		{
			InvisibleProjectionAnnotation annotation = new InvisibleProjectionAnnotation();
			newAnnotations.put(annotation, positions.get(i));
		}
		
		model.modifyAnnotations(null, newAnnotations, null);

	}

}
