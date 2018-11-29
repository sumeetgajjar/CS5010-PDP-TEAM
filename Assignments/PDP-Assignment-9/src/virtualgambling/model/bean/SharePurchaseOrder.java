package virtualgambling.model.bean;

import java.math.BigDecimal;

import util.Utils;

/**
 * This class represents a information which is captured when a Share is bought by the User.
 */
public class SharePurchaseOrder {
  private final String tickerName;
  private final long quantity;
  private final double commissionPercentage;
  private final StockPrice stockPrice;

  /**
   * Constructs an Object of Share purchase order with the given params.
   *
   * @param tickerName           the ticker name
   * @param quantity             the quantity of the shares purchased
   * @param commissionPercentage the commission percentage for this purchase
   * @throws IllegalArgumentException if any of the given params are null
   */
  public SharePurchaseOrder(String tickerName, StockPrice stockPrice, long quantity,
                            double commissionPercentage) {
    Utils.requireNonNull(stockPrice);
    this.tickerName = Utils.requireNonNull(tickerName);
    this.stockPrice = Utils.requireNonNull(stockPrice);
    this.quantity = Utils.requireNonNull(quantity);
    this.commissionPercentage = Utils.requireNonNull(commissionPercentage);
  }

  /**
   * Constructs an instance of {@link SharePurchaseOrder} with the given params.
   *
   * @param tickerName the tickerName of the purchased share
   * @param quantity   the quantity of the purchased share
   */
  public SharePurchaseOrder(String tickerName, StockPrice stockPrice, long quantity) {
    this(tickerName, stockPrice, quantity, 0D);
  }

  public SharePurchaseOrder(SharePurchaseOrder sharePurchaseOrder, double commissionPercentage) {
    this(sharePurchaseOrder.getTickerName(), sharePurchaseOrder.getStockPrice(),
            sharePurchaseOrder.getQuantity(),
            validateCommissionPercentage(commissionPercentage));
  }

  private static double validateCommissionPercentage(double commissionPercentage) {
    if (commissionPercentage < 0) {
      throw new IllegalArgumentException("Commission percentage cannot be less than 0");
    }
    return commissionPercentage;
  }

  /**
   * Returns the tickerName of the Share.
   *
   * @return the tickerName of the Share
   */
  public String getTickerName() {
    return tickerName;
  }

  /**
   * Gets {@link StockPrice} of the purchase order.
   *
   * @return {@link StockPrice} of the purchase order.
   */
  public StockPrice getStockPrice() {
    return stockPrice;
  }

  /**
   * Returns the quantity of the purchased share.
   *
   * @return the quantity of the purchased share
   */
  public long getQuantity() {
    return quantity;
  }

  /**
   * Returns the commission percentage associated with this purchase.
   *
   * @return the commission percentage associated with this purchase
   */
  public double getCommissionPercentage() {
    return commissionPercentage;
  }

  /**
   * Returns the total cost of this purchase.
   *
   * @return the total cost of this purchase
   */
  public BigDecimal getCostOfPurchase() {
    BigDecimal costOfAllShares =
            this.getStockPrice().getUnitPrice().multiply(BigDecimal.valueOf(quantity));
    BigDecimal commission = costOfAllShares.multiply(new BigDecimal(commissionPercentage / 100D));
    return costOfAllShares.add(commission);
  }

  @Override
  public String toString() {
    return String.format("Purchased %d share(s) of '%s' at a rate of %s per stock on %s",
            quantity, tickerName,
            Utils.getFormattedCurrencyNumberString(getStockPrice().getUnitPrice()),
            Utils.getDefaultFormattedDateStringFromDate(getStockPrice().getDate()));
  }
}
