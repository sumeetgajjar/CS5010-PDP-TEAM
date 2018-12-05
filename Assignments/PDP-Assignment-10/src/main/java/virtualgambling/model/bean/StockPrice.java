package virtualgambling.model.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import util.Utils;

/**
 * Represents stock price and is expressed in terms of it's date and the unit price.
 */
public class StockPrice {
  private final Date date;
  private final BigDecimal stockPrice;

  /**
   * Constructs a stockPrice in terms of it's date and stockPrice.
   *
   * @param stockPrice stock price in terms of bigDecimal
   * @param date       the date
   * @throws IllegalArgumentException if any inputs are null
   */
  public StockPrice(BigDecimal stockPrice, Date date) throws IllegalArgumentException {
    this.date = Utils.requireNonNull(date);
    this.stockPrice = Utils.requireNonNull(stockPrice);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof StockPrice)) return false;
    StockPrice that = (StockPrice) o;
    return Objects.equals(getDate(), that.getDate()) &&
            Objects.equals(stockPrice, that.stockPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getDate(), stockPrice);
  }

  /**
   * Fetches the date of the stock price.
   *
   * @return date of the stock price
   */
  public Date getDate() {
    return date;
  }

  /**
   * Fetches the unit price of the stock.
   *
   * @return unit price of the stock
   */
  public BigDecimal getUnitPrice() {
    return stockPrice;
  }
}
