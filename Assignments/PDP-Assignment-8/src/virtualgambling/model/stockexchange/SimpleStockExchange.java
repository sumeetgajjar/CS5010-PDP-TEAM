package virtualgambling.model.stockexchange;

import java.math.BigDecimal;
import java.util.Date;

import util.Utils;
import virtualgambling.model.bean.SharePurchaseInfo;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdatasource.StockDataSource;

/**
 * Created by gajjar.s, on 9:46 PM, 11/12/18
 */
public class SimpleStockExchange implements StockExchange {
  private final StockDataSource stockDataSource;

  public SimpleStockExchange(StockDataSource stockDataSource) {
    this.stockDataSource = stockDataSource;
  }

  @Override
  public SharePurchaseInfo buyShares(String tickerName, long quantity, Date date,
                                     BigDecimal remainingCapital) {
    Utils.requireNonNull(remainingCapital);
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity has to be positive");
    }
    BigDecimal stockPrice = this.getPrice(tickerName, date);
    BigDecimal costOfPurchase = stockPrice.multiply(BigDecimal.valueOf(quantity));
    if (costOfPurchase.compareTo(remainingCapital) > 0) {
      throw new IllegalStateException("Insufficient funds");
    }
    return new SharePurchaseInfo(tickerName, stockPrice, date,
            quantity);
  }

  @Override
  public BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException {
    Utils.requireNonNull(tickerName);
    Utils.requireNonNull(date);

    if (Utils.checkTimeNotInBusinessHours(date)) {
      throw new IllegalArgumentException("Cannot buy shares at given time");
    }
    return stockDataSource.getPrice(tickerName, date);
  }
}
