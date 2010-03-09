package app.web.page;

import java.util.logging.Logger;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import app.data.Order;
import app.web.EBeanStoreSession;
import app.web.layout.EBeanStorePage;
import app.web.page.OrderPage;
import app.web.panel.ViewCatalogPanel;

import com.avaje.ebean.Ebean;

/**
 * Homepage
 */
public class HomePage extends EBeanStorePage {
	private static Logger log = Logger.getLogger(HomePage.class.getName());
	private static final long serialVersionUID = 1L;

	// TODO Add any page properties or variables here

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public HomePage(final PageParameters parameters) {
		super(parameters);
		final EBeanStoreSession session = (EBeanStoreSession) getSession();
		if (parameters.getAsBoolean("clear", false)) {
			session.setCurrentCart(null);
		}
		// TODO Add your page's components here
		Ebean.find(Order.class).join("customer").where().gt("id", 0).eq(
				"status", Order.Status.NEW).ilike("customer.name", "Ro%")
				.query();

		add(new Label("ordqty", new Model<Integer>(){

			@Override
			public Integer getObject() {
				return Ebean.find(Order.class).findRowCount();
			}
			
		}).setOutputMarkupId(true));
		add(new ViewCatalogPanel("catalog"));
	}
}
