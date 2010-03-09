package app.web.page;

import junit.framework.TestCase;
import org.apache.wicket.util.tester.WicketTester;
import app.web.WicketApplication;

/**
 * Simple test using the WicketTester
 */
public class OrderPageTest extends TestCase
{
	private WicketTester tester;

	@Override
	public void setUp()
	{
		tester = new WicketTester(new WicketApplication());
	}

	public void testRenderMyPage()
	{
		//start and render the test page
		tester.startPage(OrderPage.class);

		//assert rendered page class
		tester.assertRenderedPage(OrderPage.class);
	}
}
