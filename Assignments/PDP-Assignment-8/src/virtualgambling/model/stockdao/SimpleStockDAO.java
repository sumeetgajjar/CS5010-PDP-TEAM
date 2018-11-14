package virtualgambling.model.stockdao;

import java.math.BigDecimal;
import java.util.Date;

import util.Utils;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdatasource.StockDataSource;

/**
 * <code>SimpleStockDAO</code> represents a data access object that uses any implementation of the
 * stock data source in order to access stock price data and perform operations that relate to
 * accessing stock data.
 */
public class SimpleStockDAO implements StockDAO {
  private final StockDataSource stockDataSource;

  /**
   * Constructs a stockDAO given any implementation of the stock data source.
   *
   * @param stockDataSource the stock data source object
   */
  public SimpleStockDAO(StockDataSource stockDataSource) {
    this.stockDataSource = stockDataSource;
  }

  @Override
  public SharePurchaseOrder createPurchaseOrder(String tickerName, long quantity, Date date,
                                                BigDecimal remainingCapital) throws
          IllegalArgumentException, IllegalStateException {
    Utils.requireNonNull(remainingCapital);
    BigDecimal stockPrice = this.getPrice(tickerName, date);
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity has to be positive");
    }
    BigDecimal costOfPurchase = stockPrice.multiply(BigDecimal.valueOf(quantity));
    if (costOfPurchase.compareTo(remainingCapital) > 0) {
      throw new IllegalStateException("Insufficient funds");
    }
    return new SharePurchaseOrder(tickerName, stockPrice, date,
            quantity);
  }

  @Override
  public BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException {
    Utils.requireNonNull(tickerName);
    Utils.requireNonNull(date);
    if (Utils.isFutureDate(date) || Utils.isNonWorkingDayOfTheWeek(date)) {
      throw new IllegalArgumentException("Cannot buy shares at given time");
    }
    return stockDataSource.getPrice(tickerName, date);
  }
}
