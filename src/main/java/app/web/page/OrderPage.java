package app.web.page;

import app.data.Customer;
import app.data.Order;
import io.ebean.DB;
import io.ebean.Database;
import java.util.List;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Homepage */
public class OrderPage extends WebPage {
  private static final long serialVersionUID = 1L;
  private static final Logger log = LoggerFactory.getLogger(OrderPage.class);

  // TODO Add any page properties or variables here

  /**
   * Constructor that is invoked when page is invoked without a session.
   *
   * @param parameters Page parameters
   */
  public OrderPage(final PageParameters parameters) {
    final Database server = DB.getDefault();
    log.info("server={}", server);
    log.info("brean1={}", server.createEntityBean(Customer.class));
    log.info("brean1={}", server.createEntityBean(Order.class));
    List<Customer> custset = server.find(Customer.class).findList();
    Form f = new Form("form");
    add(f);
    f.add(new DropDownChoice("cset", custset, new ChoiceRenderer("name")));
  }
}
