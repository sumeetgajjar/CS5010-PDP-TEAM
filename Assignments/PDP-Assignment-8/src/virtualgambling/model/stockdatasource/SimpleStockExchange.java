package virtualgambling.model.stockdatasource;

import java.math.BigDecimal;
import java.util.Date;

import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockexchange.StockDataSource;
import virtualgambling.util.Utils;

/**
 * Created by gajjar.s, on 9:46 PM, 11/12/18 todo: is this redundant: read about what a stock
 * exchange is
 */
public class SimpleStockExchange implements StockExchange {

  private final StockDataSource stockDataSource;

  public SimpleStockExchange(StockDataSource stockDataSource) {
    this.stockDataSource = stockDataSource;
  }

  @Override
  public BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException,
          IllegalArgumentException {

    if (Utils.checkTimeNotInBusinessHours(date)) {
      throw new IllegalArgumentException("Cannot buy stock at given time");
    }

    return stockDataSource.getPrice(tickerName, date);
  }
}
