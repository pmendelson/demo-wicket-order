package app.data;

import io.ebean.annotation.EnumValue;
import io.ebean.annotation.NotNull;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/** Order entity bean. */
@Entity
@Table(name = "o_order")
// @Sql(
//    select = {
//      @SqlSelect(name = "test", query = "select id, status from o_customer u", tableAlias = "u"),
//      @SqlSelect(name = "test2", extend = "test", where = "u.status = :status")
//    })
public class Order extends Domain {

  public enum Status {
    @EnumValue("N")
    NEW,
    @EnumValue("A")
    APPROVED,
    @EnumValue("S")
    SHIPPED,
    @EnumValue("C")
    COMPLETE
  }

  public Order() {}

  //
  //	@Formula(select="c.name",join="join o_customer c on c.id = ${ta}.kcustomer_id")
  //	String custName;

  @Enumerated(value = EnumType.ORDINAL)
  Status status = Status.NEW;

  Date orderDate = new Date(System.currentTimeMillis());

  Date shipDate;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "kcustomer_id")
  Customer customer;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
  List<OrderDetail> details = new ArrayList<OrderDetail>();

  /** Return order date. */
  public Date getOrderDate() {
    return orderDate;
  }

  /** Set order date. */
  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  /** Return ship date. */
  public Date getShipDate() {
    return shipDate;
  }

  /** Set ship date. */
  public void setShipDate(Date shipDate) {
    this.shipDate = shipDate;
  }

  /** Return status. */
  public Status getStatus() {
    return status;
  }

  /** Set status. */
  public void setStatus(Status status) {
    this.status = status;
  }

  /** Return customer. */
  public Customer getCustomer() {
    return customer;
  }

  /** Set customer. */
  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  /** Return details. */
  public List<OrderDetail> getDetails() {
    return details;
  }

  /** Set details. */
  public void setDetails(List<OrderDetail> details) {
    this.details = details;
  }
}
