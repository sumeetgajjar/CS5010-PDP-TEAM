package virtualgambling.model.stockdatasource;

import java.math.BigDecimal;
import java.time.temporal.ChronoField;
import java.util.Date;

import virtualgambling.model.bean.Share;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockexchange.StockDataSource;

/**
 * Created by gajjar.s, on 9:46 PM, 11/12/18
 */
public class SimpleStockExchange implements StockExchange {

  private final StockDataSource stockDataSource;

  public SimpleStockExchange(StockDataSource stockDataSource) {
    this.stockDataSource = stockDataSource;
  }

  @Override
  public Share buyShare(String tickerName, Date date) throws StockDataNotFoundException,
          IllegalArgumentException {

    if (this.checkTimeNotInBusinessHours(date)) {
      throw new IllegalArgumentException("Cannot buy stock at given time");
    }

    BigDecimal stockPrice = stockDataSource.getPrice(tickerName, date);
    return new Share(tickerName, stockPrice);
  }

  private boolean checkTimeNotInBusinessHours(Date date) {
    int dayOfTheWeek = date.toInstant().get(ChronoField.DAY_OF_WEEK);
    if (dayOfTheWeek < 1 || dayOfTheWeek > 5) {
      return false;
    }

    int hour = date.toInstant().get(ChronoField.HOUR_OF_DAY);
    return hour >= 8 && hour <= 15;
  }
}
