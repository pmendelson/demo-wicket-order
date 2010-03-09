package app.web.layout;

import java.util.logging.Logger;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.RepeatingView;

import app.web.page.CatalogEditPage;
import app.web.page.HomePage;
import app.web.page.OrderPage;

/**
 * Homepage
 */
public class EBeanStorePage extends WebPage {
	private static Logger log = Logger.getLogger(EBeanStorePage.class.getName());
	private static final long serialVersionUID = 1L;

	// TODO Add any page properties or variables here

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public EBeanStorePage(final PageParameters parameters) {
		WebMarkupContainer also = new WebMarkupContainer("also");
		add(also.setOutputMarkupId(true));
		RepeatingView container = new RepeatingView("alsolist");
		also.add(container.setOutputMarkupId(true));
		if (!(this instanceof HomePage))
			createAlso(container, HomePage.class, "Shopping Cart");
		if (!(this instanceof CatalogEditPage))
		createAlso(container, CatalogEditPage.class, "Update Catalog");
		createAlso(container, OrderPage.class, "Maintain Addresses");
	}

	private void createAlso(RepeatingView container, Class<? extends Page> clazz, final String txt) {
		createAlso(container, new BookmarkablePageLink("link", clazz), txt);
	}

	private void createAlso(RepeatingView container, AbstractLink link, final String txt) {
		WebMarkupContainer block = new WebMarkupContainer(container.newChildId());
		container.add(block);
		block.add(link);
		link.add(new Label("label", txt));
	}
}
