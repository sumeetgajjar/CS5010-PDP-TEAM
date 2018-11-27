package util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.EnhancedUserModelImpl;
import virtualgambling.model.SimpleUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdao.SimpleStockDAO;
import virtualgambling.model.stockdao.StockDAO;
import virtualgambling.model.stockdatasource.SimpleStockDataSource;
import virtualgambling.model.stockdatasource.StockDataSource;

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
  public static EnhancedUserModel getEmptyEnhancedUserModelWithStockDAO(StockDAO stockDAO) {
    return new EnhancedUserModelImpl(stockDAO);
  }

  /**
   * Returns a SimpleUserModel.
   *
   * @return a SimpleUserModel
   */
  public static EnhancedUserModel getEmptyEnhancedUserModel() {
    return getEmptyEnhancedUserModelWithStockDAO(new SimpleStockDAO(new SimpleStockDataSource()));
  }

  /**
   * Returns a SimpleUserModel.
   *
   * @return a SimpleUserModel
   */
  public static UserModel getEmptySimpleUserModelUsingStockDAO(StockDAO stockDAO) {
    return new SimpleUserModel(stockDAO);
  }

  /**
   * Returns a SimpleUserModel.
   *
   * @return a SimpleUserModel
   */
  public static UserModel getEmptySimpleUserModel() {
    return getEmptySimpleUserModelUsingStockDAO(new SimpleStockDAO(new SimpleStockDataSource()));
  }

  /**
   * Returns the welcome message for the App.
   *
   * @return the welcome message for the App
   */
  public static String getWelcomeMessage() {
    return "" + System.lineSeparator() + ""
            + "__        __   _                            _____      __     ___      _            "
            + "   _   _____              _ _             " + System.lineSeparator() + ""
            + "\\ \\      / /__| | ___ ___  _ __ ___   ___  |_   _|__   \\ \\   / (_)_ __| |_ _   _"
            + "  __ _| | |_   _| __ __ _  __| (_)_ __   __ _ " + System.lineSeparator() + ""
            + " \\ \\ /\\ / / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\   | |/ _ \\   \\ \\ / /| | '__| "
            + "__| | | |/ _` | |   | || '__/ _` |/ _` | | '_ \\ / _` |" + System.lineSeparator()
            + ""
            + "  \\ V  V /  __/ | (_| (_) | | | | | |  __/   | | (_) |   \\ V / | | |  | |_| |_| | "
            + "(_| | |   | || | | (_| | (_| | | | | | (_| |" + System.lineSeparator() + ""
            + "   \\_/\\_/ \\___|_|\\___\\___/|_| |_| |_|\\___|   |_|\\___/     \\_/  |_|_|   "
            + "\\__|\\__,_|\\__,_|_|   |_||_|  \\__,_|\\__,_|_|_| |_|\\__, |"
            + System.lineSeparator() + ""
            + "                                                                                    "
            + "                                    |___/ "
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + "You can use the following example commands where the first word is the "
            + System.lineSeparator() + "command and the remaining are it's parameters"
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + "create_portfolio portfolioName (portfolioName should be one word)."
            + System.lineSeparator()
            + "get_all_portfolios"
            + System.lineSeparator()
            + "get_portfolio_cost_basis portfolioName date"
            + System.lineSeparator()
            + "get_portfolio_value portfolioName date"
            + System.lineSeparator()
            + "get_portfolio_composition portfolioName"
            + System.lineSeparator()
            + "get_remaining_capital"
            + System.lineSeparator()
            + "buy_shares tickerName portfolioName date quantity"
            + System.lineSeparator()
            + "q or quit"
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + "All dates must be in this format 'yyyy-MM-DD' and the date should not be a weekend."
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + System.lineSeparator();
  }

  public static class MockUserModel extends SimpleUserModel {
    private Date mockedTodayDate;

    private MockUserModel(StockDAO stockDAO) {
      super(stockDAO);
    }

    MockUserModel(Date mockedTodayDate) throws IllegalArgumentException {
      this(new SimpleStockDAO(new MockDataSource()));
      this.mockedTodayDate = Utils.requireNonNull(mockedTodayDate);
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
      } else if (tickerName.equals("FB")) {
        return new BigDecimal("40");
      } else if (tickerName.equals("NFLX")) {
        return new BigDecimal("20");
      }

      throw new StockDataNotFoundException("Stock Data not found");
    }
  }
}
