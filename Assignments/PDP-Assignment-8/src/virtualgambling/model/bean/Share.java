package virtualgambling.model.bean;

import java.math.BigDecimal;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Share share = (Share) o;
    return Objects.equals(tickerName, share.tickerName) &&
            Objects.equals(unitPrice, share.unitPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tickerName, unitPrice);
  }
}
