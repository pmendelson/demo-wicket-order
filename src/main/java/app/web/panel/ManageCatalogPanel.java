package app.web.panel;

import java.util.List;
import java.util.logging.Logger;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.avaje.ebean.Ebean;

import app.data.Product;
import app.web.EBeanStoreSession;

/**
 * Homepage
 */
public class ManageCatalogPanel extends ViewCatalogPanel {
	private static Logger log = Logger.getLogger(ManageCatalogPanel.class.getName());
	private static final long serialVersionUID = 1L;

	public ManageCatalogPanel(String componentId) {
		super(componentId);
	}

	protected void createActionColumns(List<IColumn<Product>> r) {
		r.add(new AbstractColumn<Product>(new Model("")) {

			public void populateItem(Item<ICellPopulator<Product>> cellItem, String componentId,
					final IModel<Product> rowModel) {
				Fragment f = new Fragment(componentId, "linkWrapper", ManageCatalogPanel.this);
				AjaxLink link = new AjaxLink("link") {
					@Override
					public void onClick(AjaxRequestTarget target) {
							activateItemWindow(rowModel, "prompt.edit", target);
					}

					@Override
					protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
						replaceComponentTagBody(markupStream, openTag, "Edit");
					}
				};
				f.add(link);
				cellItem.add(f);
			}
		});
		r.add(new AbstractColumn<Product>(new Model("")) {

			public void populateItem(Item<ICellPopulator<Product>> cellItem, String componentId,
					final IModel<Product> rowModel) {
				Fragment f = new Fragment(componentId, "linkWrapper", ManageCatalogPanel.this);
				AjaxLink link = new AjaxLink("link") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						Ebean.delete(rowModel.getObject());
						refresh(target);
					}

					@Override
					protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
						replaceComponentTagBody(markupStream, openTag, "Delete");
					}
				};
				f.add(link);
				cellItem.add(f);
			}
		});
	}

	protected void createGeneralButtons(RepeatingView r) {
		r.add(createButton(r.newChildId(), new AjaxLink("button") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				activateItemWindow(new Model<Product>(new Product()), "prompt.add", target);
			}
		}, "button.add"));
	}

	private void activateItemWindow(final IModel<Product> rowModel, String promptid, AjaxRequestTarget target) {
		popup.setInitialHeight(200);
		popup.setInitialWidth(300);
		popup.setTitle(getString(promptid));
		popup.setContent(new ItemDefinitionPanel(popup.getContentId(), rowModel));
		popup.show(target);
	}
}
