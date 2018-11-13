package virtualgambling.model.bean;

import java.math.BigDecimal;

/**
 * Created by gajjar.s, on 10:11 PM, 11/11/18
 */
public class Share {
  private final String tickerName;
  private final BigDecimal unitPrice;

  public Share(String tickerName, BigDecimal unitPrice) {
    this.tickerName = tickerName;
    this.unitPrice = unitPrice;
  }

  public String getTickerName() {
    return tickerName;
  }

  public BigDecimal getUnitPrice() {
    return unitPrice;
  }
}
