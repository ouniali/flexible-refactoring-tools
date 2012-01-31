package test;

import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.eclipse.WorkbenchLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import org.eclipse.swt.custom.StyledText;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.WT;

public class WindowTester extends UITestCaseSWT {

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
	public void testWindowTester() throws Exception {
		IUIContext ui = getUI();
		ui.click(2, new XYLocator(new SWTWidgetLocator(StyledText.class), 213,
				321));
		ui.enterText("new_sb");
		ui.wait(new ShellShowingCondition(""));
		ui.keyClick(WT.CR);
		ui.keyClick(WT.CR);
	}

}