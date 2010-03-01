package app.web.panel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import app.data.Product;
import app.web.EBeanStoreSession;
import app.workflow.ShoppingCart;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.PagingList;
import com.avaje.ebean.Query;

/**
 * Homepage
 */
public class ViewCatalogPanel extends Panel {
	private static Logger log = Logger.getLogger(ViewCatalogPanel.class.getName());
	private static final long serialVersionUID = 1L;
	private static final String ID4BUTTON_BAR = "buttonbar";
	private static final String ID4PRODUCT_LIST = "plist";
	private static final String SORTBY_NAME = "name";
	private static final String SORTBY_SKU = "sku";

	private class ProductProvider extends SortableDataProvider<Product> {
		private transient Query<Product> query;
		private transient PagingList<Product> pager = null;
		private boolean orderup;
		private String sortProp;

		public Iterator<? extends Product> iterator(int first, int count) {
			if (pager == null) {
				setPager(count);
			} else {
				SortParam sort = getSort();
				if (sort != null
						&& (sort.isAscending() != orderup || !sort.getProperty().equalsIgnoreCase(sortProp)))
					setPager(pager.getPageSize());

			}
			return pager.getPage(first / pager.getPageSize()).getList().iterator();
		}

		public IModel<Product> model(Product object) {
			return new Model<Product>(object);
		}

		public int size() {
			if (pager == null) {
				return getQuery().findRowCount();
			} else
				return pager.getTotalRowCount();
		}

		private Query<Product> getQuery() {
			if (query == null)
				query = Ebean.find(Product.class);
			return query;
		}

		private void setPager(int count) {
			SortParam sort = getSort();
			orderup = sort == null || sort.isAscending();
			if (sort != null && sort.getProperty().equalsIgnoreCase(SORTBY_SKU)) {
				query.orderBy("sku" + (orderup ? "" : " desc") + ",name,id");
				sortProp = SORTBY_SKU;
			} else {
				query.orderBy("name" + (orderup ? "" : " desc") + ",sku,id");
				sortProp = SORTBY_NAME;
			}
			pager = query.findPagingList(count);
		}

		public void setFilter(String search) {
			query = Ebean.find(Product.class);
			if (search != null)
				query.where("name" + " like " + "'%" + search + "%'");
			pager = null;
		}
	}

	private String mSearch;

	public void setSearch(String v) {
		mSearch = v;
	}

	public String getSearch() {
		return mSearch;
	}

	protected ModalWindow popup;
	private transient ShoppingCart cart;

	// TODO Add any page properties or variables here

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public ViewCatalogPanel(String componentId) {
		super(componentId);
		final ProductProvider dataProvider = new ProductProvider();
		dataProvider.setSort(SORTBY_NAME, true);
		Form f = new Form("form");
		f.add(new TextField("search", new PropertyModel(this, "search")));
		f.add(new AjaxButton("search_go") {

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				dataProvider.setFilter(getSearch());
				refresh(target);
			}
		});
		add(f);
		add(popup = new ModalWindow("popup"));
		add(new DefaultDataTable(ID4PRODUCT_LIST, getColumns(), dataProvider, 3).setOutputMarkupId(true));
		add(createButtonBar(ID4BUTTON_BAR).setOutputMarkupId(true));
	}

	private Component createButtonBar(String componentId) {
		WebMarkupContainer r = new WebMarkupContainer(componentId);
		final RepeatingView buttonSet = new RepeatingView("button");
		r.add(buttonSet);
		createGeneralButtons(buttonSet);
		return r;
	}

	protected void createGeneralButtons(RepeatingView r) {
		r.add(createButton(r.newChildId(), new AjaxLink("button") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				((EBeanStoreSession) getSession()).setCurrentCart(null);
				cart = null;
				refresh(target);
			}

			@Override
			public boolean isEnabled() {
				return !getCart().isEmpty();
			}
		}, "button.clear"));
		r.add(createButton(r.newChildId(), new AjaxLink("button") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				popup.setInitialHeight(300);
				popup.setInitialWidth(450);
				popup.setTitle(getString("prompt.checkout"));
				popup.setContent(new CheckoutPanel(popup.getContentId()));
				popup.show(target);
			}

			@Override
			public boolean isEnabled() {
				return !getCart().isEmpty();
			}
		}, "button.buy"));
	}

	protected Component createButton(String id, AbstractLink link, String labelid) {
		Fragment r = new Fragment(id, "buttonWrapper", ViewCatalogPanel.this);
		r.add(link);
		link.add(new Label("label", getString(labelid)));
		return r;
	}

	public ShoppingCart getCart() {
		if (cart == null) {
			cart = new ShoppingCart(((EBeanStoreSession) (getSession())).getCurrentCart());
		}
		return cart;
	}

	private List<IColumn<Product>> getColumns() {
		List<IColumn<Product>> r = new ArrayList<IColumn<Product>>();
		r.add(new PropertyColumn<Product>(new ResourceModel("colhead.sku"), SORTBY_SKU, "sku"));
		r.add(new PropertyColumn<Product>(new ResourceModel("colhead.name"), SORTBY_NAME, "name"));
		createActionColumns(r);
		return r;
	}

	protected void createActionColumns(List<IColumn<Product>> r) {
		r.add(new AbstractColumn<Product>(new Model("")) {

			public void populateItem(Item<ICellPopulator<Product>> cellItem, String componentId,
					final IModel<Product> rowModel) {
				Fragment f = new Fragment(componentId, "linkWrapper", ViewCatalogPanel.this);
				AjaxLink link = new AjaxLink("link") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						if (getCart().hasItem(rowModel.getObject())) {
							getCart().removeItem(rowModel.getObject());
							refresh(target);
						} else {
							popup.setInitialHeight(200);
							popup.setInitialWidth(300);
							popup.setTitle(getString("prompt.add"));
							popup.setContent(new OrderItemPanel(popup.getContentId(), rowModel));
							popup.show(target);
						}

					}

					@Override
					protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
						String key = getCart().hasItem(rowModel.getObject()) ? "prompt.drop" : "prompt.add";
						replaceComponentTagBody(markupStream, openTag, getString(key));
					}
				};
				f.add(link);
				cellItem.add(f);
			}
		});
	}

	public void refresh(AjaxRequestTarget target) {
		target.addComponent(get(ID4PRODUCT_LIST));
		target.addComponent(get(ID4BUTTON_BAR));
	}
}
