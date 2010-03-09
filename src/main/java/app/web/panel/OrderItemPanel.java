package app.web.panel;

import java.util.logging.Logger;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import app.data.Product;
import app.web.EBeanStoreSession;
import app.workflow.ShoppingCart;

/**
 * Homepage
 */
public class OrderItemPanel extends Panel {
	private static Logger log = Logger.getLogger(OrderItemPanel.class.getName());
	private static final long serialVersionUID = 1L;

	// TODO Add any page properties or variables here

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public OrderItemPanel(String id) {
		super(id);
	}

	public OrderItemPanel(String contentId, final IModel<Product> model) {
		super(contentId, model);
		add(new Label("prod", model.getObject().getName()));
		final Form f = new Form("form");
		add(f);
		f.add(new TextField("qty", new Model(new Integer(1)), Integer.class));
		f.add(new AjaxSubmitLink("submit") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				ViewCatalogPanel p = findParent(ViewCatalogPanel.class);
				ShoppingCart cart = p.getCart();
				EBeanStoreSession session = (EBeanStoreSession) getSession();
				Product prod = model.getObject();
				Integer qty = (Integer) f.get("qty").getDefaultModelObject();
				cart.addItem(prod, qty);
				session.setCurrentCart(cart.getOrder());
				if (p != null) {
					p.refresh(target);
				}
				ModalWindow w = findParent(ModalWindow.class);
				if (w != null) {
					w.close(target);
				}
			}
		});

	}
}
