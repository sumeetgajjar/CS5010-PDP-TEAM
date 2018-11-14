package util;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * This class represents a Share. A share has a tickerName and a UnitPrice associated with it.
 */
public class Share {
  private final String tickerName;
  private final BigDecimal unitPrice;

  /**
   * Constructs a Object of {@link Share} with the given params.
   *
   * @param tickerName the tickerName of the share
   * @param unitPrice  the unitPrice of the share
   */
  public Share(String tickerName, BigDecimal unitPrice) {
    this.tickerName = tickerName;
    this.unitPrice = unitPrice;
  }

  /**
   * Returns the ticker name of this share.
   *
   * @return the ticker name of this share
   */
  public String getTickerName() {
    return tickerName;
  }

  /**
   * Returns the unit price of this share.
   *
   * @return the unit price of this share
   */
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
