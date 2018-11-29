package virtualgambling.model.bean;

import java.math.BigDecimal;
import java.util.Date;

public class StockPrice {
  private final Date date;
  private final BigDecimal stockPrice;

  public StockPrice(BigDecimal stockPrice, Date date) {
    this.date = date;
    this.stockPrice = stockPrice;
  }

  public Date getDate() {
    return date;
  }

  public BigDecimal getUnitPrice() {
    return stockPrice;
  }
}
