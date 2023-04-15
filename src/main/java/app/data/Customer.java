package app.data;

import io.ebean.annotation.EnumValue;
import io.ebean.annotation.Length;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/** Customer entity bean. */
@Entity
@Table(name = "o_customer")
public class Customer extends Domain {

  private static final long serialVersionUID = 1L;

  /** EnumMapping is an Ebean specific mapping for enums. */
  public enum Status {
    @EnumValue("N")
    NEW,
    @EnumValue("N")
    ACTIVE,
    @EnumValue("I")
    INACTIVE
  }

  Status status;

  @NotNull
  @Length(40)
  String name;

  @ManyToOne(cascade = CascadeType.ALL)
  Address billingAddress;

  @ManyToOne(cascade = CascadeType.ALL)
  Address shippingAddress;

  @OneToMany(mappedBy = "customer")
  List<Order> orders;

  /** Return name. */
  public String getName() {
    return name;
  }

  /** Set name. */
  public void setName(String name) {
    this.name = name;
  }

  /** Return billing address. */
  public Address getBillingAddress() {
    return billingAddress;
  }

  /** Set billing address. */
  public void setBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
  }

  /** Return status. */
  public Status getStatus() {
    return status;
  }

  /** Set status. */
  public void setStatus(Status status) {
    this.status = status;
  }

  /** Return shipping address. */
  public Address getShippingAddress() {
    return shippingAddress;
  }

  /** Set shipping address. */
  public void setShippingAddress(Address shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  /** Return orders. */
  public List<Order> getOrders() {
    return orders;
  }

  /** Set orders. */
  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }
}
