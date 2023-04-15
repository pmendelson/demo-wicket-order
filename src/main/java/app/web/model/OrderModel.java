package app.web.model;

import app.data.Order;
import io.ebean.DB;
import org.apache.wicket.model.LoadableDetachableModel;

public class OrderModel extends LoadableDetachableModel<Order> {

  private Object mId;

  @Override
  protected Order load() {
    return DB.find(Order.class, mId);
  }
}
