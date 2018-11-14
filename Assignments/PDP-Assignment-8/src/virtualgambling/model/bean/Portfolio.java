package virtualgambling.model.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents {@link Portfolio} for the User. The portfolio has a Name and a list of
 * purchases of Share associated with it.
 */
public class Portfolio {
  private final String name;
  private final List<SharePurchaseInfo> purchases;

  /**
   * Constructs a Object of {@link Portfolio} with the given name.
   *
   * @param name the name of the portfolio.
   */
  public Portfolio(String name) {
    this.name = name;
    this.purchases = new ArrayList<>();
  }

  /**
   * Returns the name of this Portfolio.
   *
   * @return the name of this Portfolio
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the list of purchases in this portfolio.
   *
   * @return the list of purchases in this portfolio
   */
  public List<SharePurchaseInfo> getPurchases() {
    return Collections.unmodifiableList(this.purchases);
  }

  /**
   * Adds the given Purchase info of the Share to this Portfolio.
   *
   * @param sharePurchaseInfo the purchase info to add
   */
  public void addPurchaseInfo(SharePurchaseInfo sharePurchaseInfo) {
    this.purchases.add(sharePurchaseInfo);
  }
}
