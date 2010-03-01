package app.workflow;

import java.util.logging.Logger;

import app.data.Order;
import app.data.OrderDetail;
import app.data.Product;

/**
 * Rudimentary logic for handling an Order in the form of a shopping cart
 * experience.
 */
public class ShoppingCart {
	private static Logger log = Logger.getLogger(ShoppingCart.class.getName());
	private static final long serialVersionUID = 1L;
	private Order mPayload;

	public ShoppingCart(Order ord) {
		mPayload = ord!=null?ord:new Order();
	}

	public OrderDetail findItem(Product object) {
		if (mPayload != null) {
			for (OrderDetail item : mPayload.getDetails()) {
				if (item.getProduct().getId() == object.getId()) {
					return item;
				}
			}
		}
		return null;
	}

	public boolean hasItem(Product sku) {
		return findItem(sku) != null;
	}

	public void removeItem(Product sku) {
		OrderDetail item = findItem(sku);
		if (item != null) {
			mPayload.getDetails().remove(item);
		}
	}

	public void addItem(Product prod, Integer qty) {
		mPayload.getDetails().add(new OrderDetail(prod, qty));
	}

	public Order getOrder() {
		return mPayload;
	}

	public boolean isEmpty() {
		return mPayload==null || mPayload.getDetails().isEmpty();
	}
}
