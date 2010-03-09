package app.web.page;

import org.apache.wicket.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.web.layout.EBeanStorePage;
import app.web.panel.ManageCatalogPanel;

/**
 * Homepage
 */
public class CatalogEditPage extends EBeanStorePage {
	private static Logger log = LoggerFactory.getLogger(CatalogEditPage.class.getName());
	private static final long serialVersionUID = 1L;

	// TODO Add any page properties or variables here

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public CatalogEditPage(final PageParameters parameters) {
		super(parameters);
		add(new ManageCatalogPanel("catalog"));
	}
}
