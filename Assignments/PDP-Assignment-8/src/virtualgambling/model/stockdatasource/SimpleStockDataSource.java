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

import virtualgambling.model.exceptions.StockDataNotFoundException;

/**
 * Created by gajjar.s, on 9:47 PM, 11/12/18
 */
public class SimpleStockDataSource implements StockDataSource {

  private static final Map<String, Map<Date, BigDecimal>> STOCK_PRICES =
          getStockPricesForLast10Days();

  @Override
  public BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException {
    BigDecimal stockPrice = STOCK_PRICES.getOrDefault(tickerName, Collections.emptyMap()).get(date);
    if (Objects.nonNull(stockPrice)) {
      return stockPrice;
    } else {
      throw new StockDataNotFoundException(
              String.format("Stock Data not found for Stock:%s for Date:%s", tickerName, date));
    }
  }

  private static Map<String, Map<Date, BigDecimal>> getStockPricesForLast10Days() {
    List<Date> dates = getDatesForLast10Days();

    List<String> stocks = Arrays.asList("AAPL", "GOOG", "GE", "BAC", "ORCL", "VZ", "MS", "T");
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

  private static List<Date> getDatesForLast10Days() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    List<Date> dates = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      dates.add(calendar.getTime());
      calendar.add(Calendar.DATE, -1);
    }

    Collections.reverse(dates);
    return dates;
  }
}
