package app.web.panel;

import app.data.Address;
import app.data.Customer;
import app.data.Order;
import app.web.EBeanStoreSession;
import app.workflow.ShoppingCart;
import io.ebean.DB;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Homepage */
public class CheckoutPanel extends Panel {
  private static Logger log = LoggerFactory.getLogger(CheckoutPanel.class);
  private static final long serialVersionUID = 1L;

  // TODO Add any page properties or variables here

  /**
   * Constructor that is invoked when page is invoked without a session.
   *
   * @param parameters Page parameters
   */
  public CheckoutPanel(String id) {
    super(id);
    final Form f = new Form("form");
    add(f);
    f.add(
        new DropDownChoice<Customer>(
            "cust", new Model(), DB.find(Customer.class).findList(), new ChoiceRenderer("name")));
    final WebMarkupContainer custwraper = new WebMarkupContainer("newcust2");
    f.add(custwraper.setOutputMarkupId(true));
    final WebMarkupContainer custmaker = new WebMarkupContainer("newcust");
    custwraper.add(custmaker.setVisible(false));
    final AjaxCheckBox checkBox =
        new AjaxCheckBox("nctoggle", new PropertyModel(custmaker, "visible")) {
          @Override
          protected void onUpdate(AjaxRequestTarget target) {
            log.warn("visibility={}  name={}", custmaker.isVisible(), custmaker.getId());
            target.addComponent(custwraper);
          }
        };
    f.add(checkBox);
    custmaker.add(new TextField("name", new Model("")));
    custmaker.add(new AddressPanel("bill"));
    f.add(
        new AjaxSubmitLink("submit") {

          @Override
          protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
            ViewCatalogPanel p = findParent(ViewCatalogPanel.class);
            ShoppingCart cart = p.getCart();
            Order order = cart.getOrder();
            Customer cust = null;
            if (custmaker.isVisible()) {
              cust = new Customer();
              cust.setName(custmaker.get("name").getDefaultModelObjectAsString());
              cust.setBillingAddress((Address) custmaker.get("bill").getDefaultModelObject());
              cust.setStatus(Customer.Status.ACTIVE);
              DB.save(cust);
            } else {
              cust = (Customer) f.get("cust").getDefaultModelObject();
            }
            order.setCustomer(cust);
            order.setStatus(Order.Status.APPROVED);
            DB.save(order);
            EBeanStoreSession session = (EBeanStoreSession) getSession();
            session.setCurrentCart(null);
            ModalWindow w = findParent(ModalWindow.class);
            if (w != null) {
              w.close(target);
              if (p != null) {
                p.refresh(target);
              }
            }
            final Component oq = getPage().get("ordqty");
            if (oq != null) target.addComponent(oq);
          }
        });
  }
}
