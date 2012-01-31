package test;

import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.eclipse.WorkbenchLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.FilteredTreeItemLocator;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Canvas;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.swt.locator.TableItemLocator;

public class Rename extends UITestCaseSWT {

	/* @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		IUIContext ui = getUI();
		ui.ensureThat(new WorkbenchLocator().hasFocus());
		ui.ensureThat(ViewLocator.forName("Welcome").isClosed());
		ui.ensureThat(new WorkbenchLocator().isMaximized());
	}

	/**
	 * Main test method.
	 */
	public void testRename() throws Exception {
		IUIContext ui = getUI();
		ui.click(new MenuItemLocator("File/Import..."));
		ui.wait(new ShellShowingCondition("Import"));
		ui.click(new FilteredTreeItemLocator(
				"General/Existing Projects into Workspace"));
		ui.click(new ButtonLocator("&Next >"));
		ui.enterText("F:/util");
		ui.keyClick(WT.CR);
		ui.click(new ButtonLocator("&Finish"));
		ui.wait(new ShellDisposedCondition("Import"));
		ui.click(
				2,
				new TreeItemLocator(
						"edu.pdx.cs.multiview.util/src/edu.pdx.cs.multiview.util.editor/AnnotationUtils.java",
						new ViewLocator("org.eclipse.jdt.ui.PackageExplorer")));
		ui.click(new XYLocator(new SWTWidgetLocator(StyledText.class,
				new SWTWidgetLocator(Canvas.class)), 149, 297));
		ui.keyClick(WT.ARROW_RIGHT);
		ui.keyClick(WT.ARROW_RIGHT);
		ui.keyClick(WT.ARROW_RIGHT);
		ui.keyClick(WT.ARROW_RIGHT);
	
		ui.wait(new ShellShowingCondition(""));
		ui.keyClick(WT.ARROW_RIGHT);
		ui.click(2, new TableItemLocator(
				"Finish rename refactoring: \"rect\" to \"rec\""));
	}

}