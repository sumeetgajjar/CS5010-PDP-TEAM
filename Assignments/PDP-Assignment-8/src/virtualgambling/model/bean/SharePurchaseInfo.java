package virtualgambling.model.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by gajjar.s, on 8:11 PM, 11/11/18
 */
public class SharePurchaseInfo {
  private final String tickerName;
  private final BigDecimal unitPrice;
  private final Date date;
  private final long quantity;

  public SharePurchaseInfo(String tickerName, BigDecimal unitPrice, Date date, long quantity) {
    this.tickerName = tickerName;
    this.unitPrice = unitPrice;
    this.date = date;
    this.quantity = quantity;
  }

  public String getTickerName() {
    return tickerName;
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public Date getDate() {
    return date;
  }

  public long getQuantity() {
    return quantity;
  }
}
