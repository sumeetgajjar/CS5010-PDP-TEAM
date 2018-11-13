package virtualgambling.model.bean;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by gajjar.s, on 8:13 PM, 11/11/18
 */
public class Portfolio {

  private final String name;
  private final List<PurchaseInfo> purchases;

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

  public BigDecimal getCostBasis() {
    return null;
  }

  public BigDecimal getPortfolioValue() {
    return null;
  }
}
