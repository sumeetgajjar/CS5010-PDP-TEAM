package virtualgambling.model.factory;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import util.Utils;
import virtualgambling.model.bean.StockPrice;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdao.SimpleStockDAO;
import virtualgambling.model.stockdao.StockDAO;
import virtualgambling.model.stockdatasource.AlphaVantageAPIStockDataSource;
import virtualgambling.model.stockdatasource.SimpleStockDataSource;
import virtualgambling.model.stockdatasource.StockDataSource;

public class StockDAOFactory {
  public static StockDAO fromStockDAOAndDataSource(StockDAOType stockDAOType,
                                                   StockDataSourceType stockDataSourceType) {
    StockDataSource stockDataSource;
    switch (stockDataSourceType) {
      case SIMPLE: {
        stockDataSource = new SimpleStockDataSource();
        break;
      }
      case ALPHA_VANTAGE: {
        stockDataSource = AlphaVantageAPIStockDataSource.getInstance();
        break;
      }
      case MOCK:
        stockDataSource = new MockDataSource();
        break;
      default:
        throw new IllegalArgumentException("Data source not found");
    }

    switch (stockDAOType) {
      case SIMPLE:
        return new SimpleStockDAO(stockDataSource);
      default:
        throw new IllegalArgumentException("stock DAO not found");
    }
  }

  private static class MockDataSource implements StockDataSource {

    @Override
    public StockPrice getPrice(String tickerName, Date date) throws StockDataNotFoundException {
      switch (tickerName) {
        case "AAPL":
          Calendar calendar = Utils.getCalendarInstance();

          calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
          Date day3 = calendar.getTime();
          if (Utils.doesDatesHaveSameDay(date, day3)) {
            return new StockPrice(BigDecimal.TEN, date);
          }

          calendar.add(Calendar.DATE, -1);
          Date day2 = calendar.getTime();
          if (Utils.doesDatesHaveSameDay(date, day2)) {
            return new StockPrice(new BigDecimal(20), date);
          }

          calendar.add(Calendar.DATE, -1);
          Date day1 = calendar.getTime();
          if (Utils.doesDatesHaveSameDay(date, day1)) {
            return new StockPrice(new BigDecimal(30), date);
          }

          calendar.set(2018, Calendar.NOVEMBER, 24);
          if (Utils.doesDatesHaveSameDay(date, calendar.getTime())) {
            return new StockPrice(new BigDecimal(1000), date);
          }

          calendar.set(2018, Calendar.OCTOBER, 24);
          if (Utils.doesDatesHaveSameDay(date, calendar.getTime())) {
            return new StockPrice(new BigDecimal(100), date);
          }

          calendar.set(2018, Calendar.SEPTEMBER, 24);
          if (Utils.doesDatesHaveSameDay(date, calendar.getTime())) {
            return new StockPrice(new BigDecimal(10), date);
          } else {
            return new StockPrice(new BigDecimal(2000), date);
          }

        case "GOOG":
          return new StockPrice(new BigDecimal("11"), date);
        case "FB":
          return new StockPrice(new BigDecimal("40"), date);
        case "NFLX":
          return new StockPrice(new BigDecimal("20"), date);
        case "T":
          return new StockPrice(new BigDecimal("10"), date);
        case "ASC":
          return new StockPrice(new BigDecimal("50"), date);
        case "MSFT":
          return new StockPrice(new BigDecimal("60"), date);
        case "AMD":
          return new StockPrice(new BigDecimal("70"), date);
        case "EBAY":
          return new StockPrice(new BigDecimal("80"), date);
        case "QCOM":
          return new StockPrice(new BigDecimal("90"), date);
        case "MU":
          return new StockPrice(new BigDecimal("100"), date);
        default:
          // stock data is not present for stocks which are not covered by above cases.
      }

      throw new StockDataNotFoundException("Stock Data not found");
    }
  }
}

