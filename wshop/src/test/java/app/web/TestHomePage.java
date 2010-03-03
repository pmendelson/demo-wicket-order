package app.web;

import junit.framework.TestCase;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.WicketTester;

import app.web.page.HomePage;

/**
 * Simple test using the WicketTester
 */
public class TestHomePage extends TestCase
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
		tester.startPage(HomePage.class);

		//assert rendered page class
		tester.assertRenderedPage(HomePage.class);

		tester.assertNoErrorMessage();
		//assert rendered label component
		tester.assertComponent("catalog:form:search",TextField.class);
		tester.assertComponent("catalog:plist",DataTable.class);
	}
}
