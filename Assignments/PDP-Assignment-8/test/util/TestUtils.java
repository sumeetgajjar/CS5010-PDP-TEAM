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

  /**
   * Returns the welcome message for the App.
   *
   * @return the welcome message for the App
   */
  public static String getWelcomeMessage() {
    return "Welcome to Virtual Stock Trading Application" + System.lineSeparator()
            + "You can use the following example commands" + System.lineSeparator()
            + "create_portfolio portfolioName (portfolioName should be one word): Create an empty"
            + " portfolio with name as portfolioName."
            + System.lineSeparator()
            + "get_all_portfolios: Gets a new line separated string of portfolio names."
            + System.lineSeparator()
            + "get_portfolio_cost_basis portfolioName date: Gets the cost basis of "
            + "portfolioName at the given date in this format --> yyyy-MM-dd."
            + System.lineSeparator()
            + "get_portfolio_value portfolioName date: Gets the value of portFolioName at the "
            + "given date in this format --> yyyy-MM-dd."
            + System.lineSeparator()
            + "get_portfolio_composition portfolioName: Gets the composition of portfolioName"
            + System.lineSeparator()
            + "get_remaining_capital: Gets your remaining in dollar amount"
            + System.lineSeparator()
            + "buy_shares tickerName portfolioName date quantity: Buys the stock with the given "
            + "ticker in portfolioName at a given date and the given quantity."
            + System.lineSeparator()
            + "The quantity must be positive and the date must be a working day not in the future.";
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
