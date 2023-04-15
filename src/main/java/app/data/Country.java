package app.data;

import io.ebean.annotation.Length;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** Country entity bean. */
@Entity
@Table(name = "o_country")
public class Country implements Serializable {

  @Id
  @Length(2)
  String code;

  @Length(60)
  String name;

  /** Return code. */
  public String getCode() {
    return code;
  }

  /** Set code. */
  public void setCode(String code) {
    this.code = code;
  }

  /** Return name. */
  public String getName() {
    return name;
  }

  /** Set name. */
  public void setName(String name) {
    this.name = name;
  }
}
