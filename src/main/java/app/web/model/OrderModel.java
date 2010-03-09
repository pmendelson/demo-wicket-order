package app.web.model;

import org.apache.wicket.model.LoadableDetachableModel;

import com.avaje.ebean.Ebean;

import app.data.Order;
import app.web.EBeanStoreSession;

public class OrderModel extends LoadableDetachableModel<Order> {

	private Object mId;

	@Override
	protected Order load() {
		return Ebean.find(Order.class,mId);
	}

}
