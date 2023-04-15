package app.web.page;

import app.data.Order;
import app.web.EBeanStoreSession;
import app.web.layout.EBeanStorePage;
import app.web.panel.ViewCatalogPanel;
import io.ebean.DB;
import java.util.logging.Logger;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

/** Homepage */
public class HomePage extends EBeanStorePage {
  private static Logger log = Logger.getLogger(HomePage.class.getName());
  private static final long serialVersionUID = 1L;

  // TODO Add any page properties or variables here

  /**
   * Constructor that is invoked when page is invoked without a session.
   *
   * @param parameters Page parameters
   */
  public HomePage(final PageParameters parameters) {
    super(parameters);
    final EBeanStoreSession session = (EBeanStoreSession) getSession();
    if (parameters.getAsBoolean("clear", false)) {
      session.setCurrentCart(null);
    }
    // TODO Add your page's components here
    DB.find(Order.class)
        .fetch("customer")
        .where()
        .gt("id", 0)
        .eq("status", Order.Status.NEW)
        .ilike("customer.name", "Ro%")
        .query();

    add(
        new Label(
                "ordqty",
                new Model<Integer>() {

                  @Override
                  public Integer getObject() {
                    return DB.find(Order.class).findCount();
                  }
                })
            .setOutputMarkupId(true));
    add(new ViewCatalogPanel("catalog"));
  }
}
