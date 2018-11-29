package virtualgambling.model.bean;

import java.math.BigDecimal;
import java.util.Date;

import util.Utils;

/**
 * This class represents a information which is captured when a Share is bought by the User.
 */
public class SharePurchaseOrder {
  private final String tickerName;
  private final BigDecimal unitPrice;
  private final Date date;
  private final long quantity;
  private final double commissionPercentage;

  /**
   * Constructs an Object of Share purchase order with the given params.
   *
   * @param tickerName           the ticker name
   * @param unitPrice            the unit price of the stock
   * @param date                 the date at which the purchase is made
   * @param quantity             the quantity of the shares purchased
   * @param commissionPercentage the commission percentage for this purchase
   * @throws IllegalArgumentException if any of the given params are null
   */
  public SharePurchaseOrder(String tickerName, BigDecimal unitPrice, Date date, long quantity,
                            double commissionPercentage) {
    this.tickerName = Utils.requireNonNull(tickerName);
    this.unitPrice = Utils.requireNonNull(unitPrice);
    this.date = Utils.requireNonNull(date);
    this.quantity = Utils.requireNonNull(quantity);
    this.commissionPercentage = Utils.requireNonNull(commissionPercentage);
  }

  /**
   * Constructs an instance of {@link SharePurchaseOrder} with the given params.
   *
   * @param tickerName the tickerName of the purchased share
   * @param unitPrice  the unit price of the purchased share
   * @param date       the date at which the share was purchased
   * @param quantity   the quantity of the purchased share
   */
  public SharePurchaseOrder(String tickerName, BigDecimal unitPrice, Date date, long quantity) {
    this(tickerName, unitPrice, date, quantity, 0D);
  }

  public SharePurchaseOrder(SharePurchaseOrder sharePurchaseOrder, double commissionPercentage) {
    this(sharePurchaseOrder.getTickerName(), sharePurchaseOrder.getUnitPrice(),
            sharePurchaseOrder.getDate(), sharePurchaseOrder.getQuantity(),
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
   * Returns the unit price of the Share.
   *
   * @return the unit price of the Share
   */
  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  /**
   * Returns the date at which the share was purchased.
   *
   * @return the date at which the share was purchased
   */
  public Date getDate() {
    return date;
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
    BigDecimal costOfAllShares = this.unitPrice.multiply(BigDecimal.valueOf(quantity));
    BigDecimal commission = costOfAllShares.multiply(new BigDecimal(commissionPercentage / 100D));
    return costOfAllShares.add(commission);
  }

  @Override
  public String toString() {
    return String.format("Purchased %d share(s) of '%s' at a rate of %s per stock on %s",
            quantity, tickerName, Utils.getFormattedCurrencyNumberString(unitPrice),
            Utils.getDefaultFormattedDateStringFromDate(date));
  }
}
