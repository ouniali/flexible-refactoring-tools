package animation.folding;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;

import org.eclipse.jdt.ui.text.folding.IJavaFoldingStructureProvider;
import org.eclipse.jface.text.ITextInputListener;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.ITextPresentationListener;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.projection.IProjectionListener;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class FoldingStructureProvider implements IJavaFoldingStructureProvider, IProjectionListener{

	ITextEditor editor;
	ProjectionViewer viewer;
	boolean installed = false;
	FoldingElementChangeListener listener;
	
	@Override
	public void initialize() {	
		
	}

	@Override
	public void install(ITextEditor e, ProjectionViewer v) {
		
		this.editor = e;
		this.viewer = v;
		installed = true;
		viewer.addProjectionListener(this);
	}

	@Override
	public void uninstall() {
	}

	
	private boolean isInstalled() {
		return installed;
	}

	@Override
	public void projectionEnabled() {
		projectionDisabled();
		if (editor instanceof JavaEditor) {
			initialize();
			listener = new FoldingElementChangeListener(editor, viewer);
			JavaCore.addElementChangedListener(listener);
		//	testingFolding();
		}
	}

	@Override
	public void projectionDisabled() {
		
		if (listener != null) {
			JavaCore.removeElementChangedListener(listener);
			listener = null;
		}
	}
		
	
	private void testingFolding()
	{
		FoldingTester tester = new FoldingTester(listener);
		new Thread(tester).start();
	}

	
	
}
