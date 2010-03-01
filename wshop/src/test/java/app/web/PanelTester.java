package app.web;

import java.lang.reflect.Constructor;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import app.web.panel.CheckoutPanel;
import app.web.panel.ViewCatalogPanel;

public class PanelTester {
	@Test
	public void testAllWicketPanels() throws Exception {
		// if(true)
		// Setup.resetData();
		WicketTester wicketTester = new WicketTester(new WicketApplication());
		testWicketPanel(wicketTester, ViewCatalogPanel.class);
		testWicketPanel(wicketTester, CheckoutPanel.class);

		// ClassPathScanningCandidateComponentProvider provider = new
		// ClassPathScanningCandidateComponentProvider(true);
		// provider.addIncludeFilter(new AssignableTypeFilter(Panel.class));
		// 
		// Set components = provider.findCandidateComponents("nl/stuq/demo");
		// for (BeanDefinition component : components) {
		// Class clazz = Class.forName(component.getBeanClassName());
		// if(hasDefaultConstructor(clazz)){
		// testWicketPanel(wicketTester, clazz);
		// }
		// }
	}

	private void testWicketPanel(WicketTester wicketTester, Class clazz) {
		wicketTester.startPanel(clazz);
		wicketTester.assertNoErrorMessage();
		wicketTester.assertNoInfoMessage();
	}

	private boolean hasDefaultConstructor(Class clazz) {
		for (Constructor constructor : clazz.getConstructors()) {
			if (constructor.getParameterTypes().length == 1
					&& constructor.getParameterTypes()[0].getSimpleName()
							.equals("String")) {
				return true;
			}
		}

		return false;
	}
}