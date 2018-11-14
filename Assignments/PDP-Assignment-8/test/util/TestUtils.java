package util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import virtualgambling.model.SimpleUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdatasource.SimpleStockDataSource;
import virtualgambling.model.stockdatasource.StockDataSource;
import virtualgambling.model.stockexchange.SimpleStockExchange;
import virtualgambling.model.stockexchange.StockExchange;

/**
 * This class represents set of util functions which can be used by any class for testing purposes.
 */
public class TestUtils {

  public static final BigDecimal DEFAULT_USER_CAPITAL = new BigDecimal("10000000");

  /**
   * Returns a mocked user model for testing purposes.
   *
   * @return a mocked user model for testing purposes
   */
  public static UserModel getMockedUserModel() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1);
    return new MockUserModel(calendar.getTime());
  }

  /**
   * Returns a SimpleUserModel.
   *
   * @return a SimpleUserModel
   */
  public static UserModel getEmptyUserModel() {
    return new SimpleUserModel(new SimpleStockExchange(new SimpleStockDataSource()));
  }

  public static class MockUserModel extends SimpleUserModel {
    private Date mockedTodayDate;

    private MockUserModel(StockExchange stockExchange) {
      super(stockExchange);
    }

    MockUserModel(Date mockedTodayDate) throws IllegalArgumentException {
      this(new SimpleStockExchange(new MockDataSource()));
      this.mockedTodayDate = Utils.requireNonNull(mockedTodayDate);
    }

    @Override
    protected Date getTodayDate() {
      return this.mockedTodayDate;
    }
  }

  public static class MockDataSource implements StockDataSource {

    @Override
    public BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException {
      if (tickerName.equals("AAPL")) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
        Date day3 = calendar.getTime();
        if (Utils.doesDatesHaveSameDay(date, day3)) {
          return BigDecimal.TEN;
        }

        calendar.add(Calendar.DATE, -1);
        Date day2 = calendar.getTime();
        if (Utils.doesDatesHaveSameDay(date, day2)) {
          return new BigDecimal(20);
        }

        calendar.add(Calendar.DATE, -1);
        Date day1 = calendar.getTime();
        if (Utils.doesDatesHaveSameDay(date, day1)) {
          return new BigDecimal(30);
        }

      } else if (tickerName.equals("GOOG")) {
        return new BigDecimal("11");
      }

      throw new StockDataNotFoundException("Stock Data not found");
    }
  }
}
