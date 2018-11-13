package virtualgambling.model.bean;

import java.util.Collections;
import java.util.List;

/**
 * Created by gajjar.s, on 8:13 PM, 11/11/18
 */
//todo see if this can be extracted as interface
public class Portfolio {

  private final String name;
  private final List<PurchaseInfo> purchases;

  //todo need to pass exchange to this for getting the portfolio value
  public Portfolio(String name, List<PurchaseInfo> purchases) {
    this.name = name;
    this.purchases = purchases;
  }

  public String getName() {
    return this.name;
  }

  public List<PurchaseInfo> getPurchases() {
    return Collections.unmodifiableList(this.purchases);
  }
}
