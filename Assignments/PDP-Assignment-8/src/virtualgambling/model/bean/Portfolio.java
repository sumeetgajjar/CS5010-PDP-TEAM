package virtualgambling.model.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gajjar.s, on 8:13 PM, 11/11/18
 */
//todo see if this can be extracted as interface
public class Portfolio {

  private final String name;
  private final List<PurchaseInfo> purchases;

  public Portfolio(String name) {
    this.name = name;
    this.purchases = new ArrayList<>();
  }

  public String getName() {
    return this.name;
  }

  public List<PurchaseInfo> getPurchases() {
    return Collections.unmodifiableList(this.purchases);
  }

  PurchaseInfo addPurchaseInfo(PurchaseInfo purchaseInfo) {
    return null;
  }
}
