package virtualgambling.model.bean;

import java.math.BigDecimal;
import java.util.Date;

import util.Utils;

/**
 * This class represents a information which is captured when a Share is purchased.
 */
public class SharePurchaseInfo {
  private final String tickerName;
  private final BigDecimal unitPrice;
  private final Date date;
  private final long quantity;

  /**
   * Constructs a Object of {@link SharePurchaseInfo} with the given params.
   *
   * @param tickerName the name of the share purchased
   * @param unitPrice  the unit price of the share purchased
   * @param date       the date at which the share was purchased
   * @param quantity   the quantity of the share purchased
   */
  public SharePurchaseInfo(String tickerName, BigDecimal unitPrice, Date date, long quantity) {
    this.tickerName = tickerName;
    this.unitPrice = unitPrice;
    this.date = date;
    this.quantity = quantity;
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
   * Returns the quantity of the shares purchased.
   *
   * @return the quantity of the shares purchased
   */
  public long getQuantity() {
    return quantity;
  }

  /**
   * Returns the total cost of this purchase.
   *
   * @return the total cost of this purchase
   */
  public BigDecimal getCostOfPurchase() {
    return this.unitPrice.multiply(BigDecimal.valueOf(quantity));
  }

  @Override
  public String toString() {
    return String.format("Purchased %d share(s) of '%s' at a rate of %s per stock on %s",
            quantity, tickerName, Utils.getFormattedCurrencyNumberString(unitPrice),
            Utils.getDefaultFormattedDateStringFromDate(date));
  }
}
