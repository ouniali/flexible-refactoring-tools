package test;

import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.eclipse.WorkbenchLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;
import com.windowtester.runtime.swt.locator.SWTWidgetLocator;
import org.eclipse.swt.custom.StyledText;
import com.windowtester.runtime.locator.XYLocator;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.locator.TableItemLocator;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.swt.locator.ShellLocator;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;

public class RoundRename extends UITestCaseSWT {

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
//	@org.JUnit.Test
	public void testRoundRename() throws Exception {
		IUIContext ui = getUI();
		ui.click(2, new XYLocator(new SWTWidgetLocator(StyledText.class), 214,
				215));
		ui.wait(new ShellShowingCondition(""));
		ui.enterText("sb");
		ui.click(2, new TableItemLocator(
				"Finish rename refactoring: \"new_sb\" to \"sb\""));
		ui.wait(new ShellShowingCondition(""));
		ui.click(2, new XYLocator(new SWTWidgetLocator(StyledText.class), 203,
				200));
		ui.enterText("new_sb");
		ui.keyClick(WT.CR);
		ui.ensureThat(new ShellLocator(
				"Java - tomcat/java/org/apache/catalina/deploy/ContextResource.java - Eclipse SDK")
				.isClosed());
		ui.keyClick(WT.CR);
		ui.wait(new ShellDisposedCondition("Eclipse SDK"));
	}

}