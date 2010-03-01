package app.web.panel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.data.Product;

import com.avaje.ebean.Ebean;

/**
 * Homepage
 */
public class ItemDefinitionPanel extends Panel {
	private static Logger log = LoggerFactory.getLogger(ItemDefinitionPanel.class.getName());
	private static final long serialVersionUID = 1L;

	// TODO Add any page properties or variables here

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public ItemDefinitionPanel(String id) {
		super(id);
	}

	public ItemDefinitionPanel(String contentId, final IModel<Product> model) {
		super(contentId, model);
		final Form f = new Form("form");
		add(f);
		final Product item = model.getObject();
		f.add(new TextField("sku", new PropertyModel(item, "sku")));
		f.add(new TextField("name", new PropertyModel(item, "name")));
		f.add(new AjaxSubmitLink("submit") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				ViewCatalogPanel p = findParent(ViewCatalogPanel.class);
				log.info("item  ID={}   sku={}   name={}", new Object[] { item.getId(), item.getSku(),
						item.getName() });
				Ebean.save(item);
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
