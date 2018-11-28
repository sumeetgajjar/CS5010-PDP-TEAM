package virtualgambling.model.stockdatasource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import util.Utils;
import virtualgambling.model.exceptions.StockDataNotFoundException;

/**
 * <code>SimpleStockDataSource</code> implements the <code>StockDataSource</code> interface and
 * provides data from an in memory source of stock prices for the last ten days.
 *
 * <p>It provides stock prices for the last 100 days of the following tickers ["AAPL", "GOOG",
 * "GE", "BAC", "ORCL", "VZ", "MS", "T"].
 */
public class SimpleStockDataSource implements StockDataSource {

  private static final Map<String, Map<Date, BigDecimal>> STOCK_PRICES =
          getStockPricesForLast10Days();

  /**
   * Retrieves the stock price information for a given stock ticker and date.
   *
   * <p>{@link SimpleStockDataSource} only considers the date and not the time.
   *
   * @param tickerName the ticker name of the stock
   * @param date       the date when the stock needs to be purchased
   * @return the price of the stock that matches the given ticker and date
   * @throws StockDataNotFoundException if stock price for the ticker for the given date is not
   *                                    found
   */
  @Override
  public BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException {
    if (Utils.isFutureDate(date) || Utils.isNonWorkingDayOfTheWeek(date)) {
      throw new IllegalArgumentException("Cannot buy shares at given time");
    }
    BigDecimal stockPrice = STOCK_PRICES.getOrDefault(tickerName, Collections.emptyMap()).get(date);
    if (Objects.nonNull(stockPrice)) {
      return stockPrice;
    } else {
      throw new StockDataNotFoundException(
              String.format("Stock Data not found for Stock:%s for Date:%s", tickerName, date));
    }
  }

  private static Map<String, Map<Date, BigDecimal>> getStockPricesForLast10Days() {
    List<Date> dates = getDatesForLast100Days();

    List<String> stocks = Arrays.asList("AAPL", "GOOG", "GE", "BAC", "ORCL", "VZ", "MS", "T", "FB"
            , "NFLX");
    BigDecimal stockPrice = new BigDecimal(10);
    Map<String, Map<Date, BigDecimal>> stockPrices = new LinkedHashMap<>();
    for (String stockName : stocks) {
      for (Date date : dates) {

        Map<Date, BigDecimal> dateToPriceMap =
                stockPrices.getOrDefault(stockName, new LinkedHashMap<>());
        dateToPriceMap.put(date, stockPrice);

        stockPrice = stockPrice.add(BigDecimal.TEN);
        stockPrices.put(stockName, dateToPriceMap);
      }
    }
    return stockPrices;
  }

  private static List<Date> getDatesForLast100Days() {
    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    List<Date> dates = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      dates.add(calendar.getTime());
      calendar.add(Calendar.DATE, -1);
    }

    Collections.reverse(dates);
    return dates;
  }
}
