package util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.EnhancedUserModelImpl;
import virtualgambling.model.SimpleUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.bean.StockPrice;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdao.SimpleStockDAO;
import virtualgambling.model.stockdao.StockDAO;
import virtualgambling.model.stockdatasource.StockDataSource;
import virtualgambling.model.strategy.RecurringWeightedInvestmentStrategy;

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
    Calendar calendar = Utils.getCalendarInstance();
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
    return getEmptyEnhancedUserModelWithStockDAO(new SimpleStockDAO(new MockDataSource()));
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
    return getEmptySimpleUserModelUsingStockDAO(new SimpleStockDAO(new MockDataSource()));
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
            + System.lineSeparator();
  }

  /**
   * Returns the menu message of the OrchestratorController.
   *
   * @return the menu message of the OrchestratorController
   */
  public static String getMenuMessageOfOrchestrator() {
    return "Please enter the data source option" + System.lineSeparator()
            + "Enter 1 for 'in-memory'" + System.lineSeparator()
            + "Enter 2 for 'alpha-vantage-api'" + System.lineSeparator()
            + "Note: Option 1 provides stock prices for the last 100 days of the following "
            + "tickers [\"AAPL\", \"GOOG\", \"GE\", \"BAC\", \"ORCL\", \"VZ\", \"MS\", \"T\"]"
            + System.lineSeparator()
            + "Note: Option 2 may lead to longer running time for operations like buying stocks, " +
            "querying composition or value of a portfolio"
            + System.lineSeparator()
            + "Note: If any given day is a holiday, Option 2 uses the next available working day " +
            "and Option 1 will be unable to find stock data."
            + System.lineSeparator()
            + "Note: All the stock prices are the closing price for the given date."
            + System.lineSeparator();
  }

  /**
   * Returns the menu message of Trading Controller.
   *
   * @return the menu message of Trading Controller
   */
  public static String getMenuStringOfTradingController() {
    return System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + "1 => to create a portfolio"
            + System.lineSeparator()
            + "2 => to list all portfolios"
            + System.lineSeparator()
            + "3 => to get the cost basis of a portfolio"
            + System.lineSeparator()
            + "4 => to get the value of a portfolio"
            + System.lineSeparator()
            + "5 => to get the composition of a portfolio"
            + System.lineSeparator()
            + "6 => to get the remaining capital"
            + System.lineSeparator()
            + "7 => to buy shares"
            + System.lineSeparator()
            + "q or quit => to quit"
            + System.lineSeparator()
            + "Please enter a choice"
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + "All dates must be in this format 'yyyy-MM-DD'"
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator();
  }

  /**
   * Returns the menu message of the EnhanceTradingController.
   *
   * @return the menu message of the EnhanceTradingController
   */
  public static String getMenuMessageOfEnhanceTradingController() {
    return System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + "1 => to create a portfolio"
            + System.lineSeparator()
            + "2 => to list all portfolios"
            + System.lineSeparator()
            + "3 => to get the cost basis of a portfolio"
            + System.lineSeparator()
            + "4 => to get the value of a portfolio"
            + System.lineSeparator()
            + "5 => to get the composition of a portfolio"
            + System.lineSeparator()
            + "6 => to get the remaining capital"
            + System.lineSeparator()
            + "7 => to buy shares of same stock"
            + System.lineSeparator()
            + "8 => to buy shares of various stocks with different individual weights"
            + System.lineSeparator()
            + "9 => to buy shares of various stocks with equal weights"
            + System.lineSeparator()
            + "10 => to recurrently buy shares of various stocks with different individual weights"
            + System.lineSeparator()
            + "11 => to recurrently buy shares of various stocks with same individual weights"
            + System.lineSeparator()
            + "q or quit => to quit"
            + System.lineSeparator()
            + "Please enter a choice"
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator()
            + "All dates must be in this format 'yyyy-MM-DD'"
            + System.lineSeparator()
            + "While buying multiple stocks, if same stock is entered multiple times then the " +
            "latest input will considered"
            + System.lineSeparator()
            + "=================================================================================="
            + System.lineSeparator();
  }

  /**
   * Returns a valid date for trading, which is 1 Nov 2018.
   *
   * @return a valid date for trading which is 1 Nov 2018
   */
  public static Date getValidDateForTrading() {
    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
    return calendar.getTime();
  }

  /**
   * Returns the tomorrow's date.
   *
   * @return the tomorrow's date
   */
  public static Date getFutureTime() {
    Calendar calendar = Utils.getCalendarInstance();
    calendar.add(Calendar.DATE, 1);
    return calendar.getTime();
  }

  public static class MockPortfolio extends Portfolio {
    private Date mockedTodayDate;

    /**
     * Constructs a Object of {@link Portfolio} with the given name.
     *
     * @param name            the name of the portfolio.
     * @param mockedTodayDate mocked date for testing
     */
    public MockPortfolio(String name, StockDAO stockDAO, List<SharePurchaseOrder> purchases,
                         Date mockedTodayDate) {
      super(name, stockDAO, purchases);
      this.mockedTodayDate = mockedTodayDate;
    }

    @Override
    protected Date getTodayDate() {
      return mockedTodayDate;
    }
  }

  public static class MockRecurringWeightedInvestmentStrategy
          extends RecurringWeightedInvestmentStrategy {

    private Date mockedYesterdayDate;

    // only constructor without explicit end date
    public MockRecurringWeightedInvestmentStrategy(Date startDate,
                                                   Map<String, Double> stockWeights,
                                                   int dayFrequency, Date mockedYesterdayDate) {
      super(startDate, stockWeights, dayFrequency);
      this.mockedYesterdayDate = Utils.requireNonNull(mockedYesterdayDate);
    }

    @Override
    protected Date getDefaultEndDate() {
      return this.mockedYesterdayDate;
    }
  }

  public static class MockUserModel extends SimpleUserModel {
    private Date mockedTodayDate;
    private StockDAO simpleStockDAO;

    private MockUserModel(StockDAO stockDAO) {
      super(stockDAO);
      this.simpleStockDAO = stockDAO;
    }

    MockUserModel(Date mockedTodayDate) throws IllegalArgumentException {
      this(new SimpleStockDAO(new MockDataSource()));
      this.mockedTodayDate = Utils.requireNonNull(mockedTodayDate);
    }

    @Override
    protected Portfolio instantiatePortfolio(String portfolioName,
                                             List<SharePurchaseOrder> sharePurchaseOrders) {
      return new MockPortfolio(portfolioName, simpleStockDAO, sharePurchaseOrders, mockedTodayDate);
    }
  }

  public static class MockDataSource implements StockDataSource {

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
