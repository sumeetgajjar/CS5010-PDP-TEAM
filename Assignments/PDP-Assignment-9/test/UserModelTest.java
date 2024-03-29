import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import util.Share;
import util.TestUtils;
import util.Utils;
import virtualgambling.model.UserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.bean.StockPrice;
import virtualgambling.model.exceptions.InsufficientCapitalException;
import virtualgambling.model.exceptions.PortfolioNotFoundException;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdao.SimpleStockDAO;
import virtualgambling.model.stockdao.StockDAO;
import virtualgambling.model.stockdatasource.SimpleStockDataSource;

/**
 * The class represents a Junit class to test Model in isolation.
 */
public class UserModelTest {

  protected UserModel getUserModel(StockDAO stockDAO) {
    return TestUtils.getEmptySimpleUserModelUsingStockDAO(stockDAO);
  }

  private UserModel getUserModelWithSimpleStockDao() {
    return getUserModel(new SimpleStockDAO(new SimpleStockDataSource()));
  }

  @Test
  public void testInitializationOfUserModel() {
    UserModel userModel = getUserModelWithSimpleStockDao();
    try {
      userModel.getPortfolio("test");
      Assert.fail("should have failed");
    } catch (PortfolioNotFoundException e) {
      Assert.assertEquals("portfolio by the name 'test' not found", e.getMessage());
    }

    userModel.createPortfolio("test");

    Date date = new Date();

    Assert.assertEquals("Buy Date            Stocks              Quantity            Cost Price  " +
            "        Current Value       Commission Percentage\n" +
            "\n" +
            "Total Value:                                      $0.00\n" +
            "Total Cost (excluding commission):                $0.00\n" +
            "Total Cost (including commission):                $0.00\n" +
            "Profit:                                           $0.00", userModel.getPortfolio(
            "test").toString());
    Assert.assertEquals(BigDecimal.ZERO,
            userModel.getPortfolio("test").getCostBasisIncludingCommission(date));
    Assert.assertEquals(BigDecimal.ZERO, userModel.getPortfolio("test").getValue(date));

    Assert.assertEquals(TestUtils.DEFAULT_USER_CAPITAL, userModel.getRemainingCapital());
  }

  @Test
  public void createPortfolioWorks() {
    UserModel userModel = getUserModelWithSimpleStockDao();
    userModel.createPortfolio("Hello world");
    Assert.assertEquals("Buy Date            Stocks              Quantity            Cost Price  " +
            "        Current Value       Commission Percentage\n" +
            "\n" +
            "Total Value:                                      $0.00\n" +
            "Total Cost (excluding commission):                $0.00\n" +
            "Total Cost (including commission):                $0.00\n" +
            "Profit:                                           $0.00", userModel.getPortfolio(
            "Hello world").toString());
  }

  @Test
  public void createPortfolioFails() {
    UserModel userModel = getUserModelWithSimpleStockDao();
    try {
      userModel.createPortfolio(null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      userModel.createPortfolio("");
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid Portfolio Name", e.getMessage());
    }

    try {
      userModel.createPortfolio(" ");
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid Portfolio Name", e.getMessage());
    }

    try {
      userModel.createPortfolio(" a");
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid Portfolio Name", e.getMessage());
    }

    try {
      userModel.createPortfolio("a ");
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid Portfolio Name", e.getMessage());
    }
  }

  @Test
  public void buyingSharesOfInvalidQuantityFails() throws IllegalArgumentException,
          StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    Date date = TestUtils.getValidDateForTrading();
    Share appleShare = getAppleShare();

    userModel.createPortfolio("p1");
    try {
      userModel.buyShares(appleShare.getTickerName(), "p1", date, 0);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Quantity has to be positive", e.getMessage());
    }

    try {
      userModel.buyShares(appleShare.getTickerName(), "p1", date, -1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Quantity has to be positive", e.getMessage());
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void cannotCreateInvalidDate() {
    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(2018, Calendar.SEPTEMBER, 31, 10, 1);
    calendar.getTime();
  }

  @Test
  public void buyingShareFailsForNullInputs() throws StockDataNotFoundException {
    UserModel userModel = getUserModelWithEmptyPortfolio();
    Date date = TestUtils.getValidDateForTrading();
    Share appleShare = getAppleShare();

    try {
      userModel.buyShares(null, "p1", date, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      userModel.buyShares(appleShare.getTickerName(), null, date, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      userModel.buyShares(appleShare.getTickerName(), "p1", null, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
  }

  @Test
  public void buyingShareForMissingPortfolioFails() throws StockDataNotFoundException {
    UserModel userModel = getUserModelWithSimpleStockDao();
    Date date = TestUtils.getValidDateForTrading();
    Share appleShare = getAppleShare();

    try {
      userModel.buyShares(appleShare.getTickerName(), "p1", date, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Portfolio not found", e.getMessage());
    }
  }

  @Test
  public void buyingSharesAtInvalidTimeFails() throws StockDataNotFoundException {
    UserModel userModel = getUserModel(new SimpleStockDAO(new SimpleStockDataSource()));
    Share appleShare = getAppleShare();
    userModel.createPortfolio("p1");

    try {
      Date weekendDate = getWeekendDate();
      userModel.buyShares(appleShare.getTickerName(), "p1", weekendDate, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Cannot buy shares at given time", e.getMessage());
    }

    Date beforeOpeningTime = getDateBeforeOpeningTime();
    userModel.buyShares(appleShare.getTickerName(), "p1", beforeOpeningTime, 1);

    Date afterClosingTime = getDateAfterClosingTime();
    userModel.buyShares(appleShare.getTickerName(), "p1", afterClosingTime, 1);


    try {
      Date futureTime = TestUtils.getFutureTime();
      userModel.buyShares(appleShare.getTickerName(), "p1", futureTime, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Time cannot be in Future", e.getMessage());
    }
  }

  @Test
  public void getRemainingCapitalWorks() throws StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    Date date = TestUtils.getValidDateForTrading();
    Share appleShare = getAppleShare();
    userModel.createPortfolio("p1");

    Assert.assertEquals(TestUtils.DEFAULT_USER_CAPITAL, userModel.getRemainingCapital());

    userModel.buyShares("AAPL", "p1", date, 1);
    Assert.assertEquals(TestUtils.DEFAULT_USER_CAPITAL.subtract(appleShare.getUnitPrice()),
            userModel.getRemainingCapital());
  }

  @Test
  public void buyingStockWhoseDataIsNotPresentFails() throws StockDataNotFoundException {
    UserModel userModel = getUserModelWithSimpleStockDao();
    userModel.createPortfolio("p1");
    try {
      Calendar calendar = Utils.getCalendarInstance();
      calendar.set(2018, Calendar.JULY, 4, 10, 0);
      calendar.add(Calendar.DATE, -1);
      Date date = Utils.removeTimeFromDate(calendar.getTime());

      userModel.buyShares(getAppleShare().getTickerName(), "p1", date, 1);
      Assert.fail("should have failed");
    } catch (StockDataNotFoundException e) {
      Assert.assertEquals(
              "Stock Data not found for Stock:AAPL for Date:Tue Jul 03 00:00:00 EDT 2018",
              e.getMessage());
    }
  }

  @Test
  public void buyingFailsForInvalidTickerName() throws StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");
    try {
      userModel.buyShares("AAPL1", "p1", TestUtils.getValidDateForTrading(), 1);
      Assert.fail("should have failed");
    } catch (StockDataNotFoundException e) {
      Assert.assertEquals("Stock Data not found", e.getMessage());
    }
  }

  @Test
  public void buyFailsDueToInsufficientFunds() throws StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    Date date = TestUtils.getValidDateForTrading();
    userModel.createPortfolio("p1");

    try {
      userModel.buyShares(getAppleShare().getTickerName(), "p1", date,
              TestUtils.DEFAULT_USER_CAPITAL
                      .divide(BigDecimal.TEN, BigDecimal.ROUND_CEILING).longValue() + 1);
      Assert.fail("should have failed");
    } catch (InsufficientCapitalException e) {
      Assert.assertEquals("Insufficient funds", e.getMessage());
    }
  }

  @Test
  public void getCostBasisFailsDueToInvalidArguments() throws StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");
    Date date = TestUtils.getValidDateForTrading();
    userModel.buyShares(getAppleShare().getTickerName(), "p1", date, 1);

    try {
      userModel.getPortfolio(null).getCostBasisIncludingCommission(date);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      userModel.getPortfolio("p1").getCostBasisIncludingCommission(null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
  }

  @Test
  public void getPortfolioValueFailsDueToInvalidArguments() throws StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");
    Date date = TestUtils.getValidDateForTrading();
    userModel.buyShares(getAppleShare().getTickerName(), "p1", date, 1);

    try {
      userModel.getPortfolio(null).getValue(date);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      userModel.getPortfolio("p1").getValue(null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
  }

  @Test
  public void buyMultipleStocksWorks() throws IllegalArgumentException, StockDataNotFoundException {
    Share appleShare = getAppleShare();
    Share googleShare = getGoogleShare();

    Date date = TestUtils.getValidDateForTrading();

    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");
    userModel.createPortfolio("p2");
    userModel.createPortfolio("p3");

    userModel.buyShares(appleShare.getTickerName(), "p1", date, 1);
    userModel.buyShares(appleShare.getTickerName(), "p3", date, 1);
    userModel.buyShares(googleShare.getTickerName(), "p2", date, 1);

    Assert.assertEquals(new BigDecimal(10),
            userModel.getPortfolio("p1").getCostBasisIncludingCommission(date));
    Assert.assertEquals(new BigDecimal(10), userModel.getPortfolio("p1").getValue(date));
    String expectedApplePurchaseComposition = "Buy Date            Stocks              Quantity  " +
            "          Cost Price          Current Value       Commission Percentage\n" +
            "2018-11-01          AAPL                1                   $10.00              $10" +
            ".00              0.0%\n" +
            "\n" +
            "Total Value:                                      $10.00\n" +
            "Total Cost (excluding commission):                $10.00\n" +
            "Total Cost (including commission):                $10.00\n" +
            "Profit:                                           $0.00";

    String expectedGooglePurchase = "Buy Date            Stocks              Quantity            " +
            "Cost Price          Current Value       Commission Percentage\n" +
            "2018-11-01          GOOG                1                   $11.00              $11" +
            ".00              0.0%\n" +
            "\n" +
            "Total Value:                                      $11.00\n" +
            "Total Cost (excluding commission):                $11.00\n" +
            "Total Cost (including commission):                $11.00\n" +
            "Profit:                                           $0.00";
    Assert.assertEquals(expectedApplePurchaseComposition, userModel.getPortfolio("p1").toString());

    Assert.assertEquals(new BigDecimal(11),
            userModel.getPortfolio("p2").getCostBasisIncludingCommission(date));
    Assert.assertEquals(new BigDecimal(11), userModel.getPortfolio("p2").getValue(date));
    Assert.assertEquals(expectedGooglePurchase, userModel.getPortfolio("p2").toString());

    Assert.assertEquals(new BigDecimal(10),
            userModel.getPortfolio("p3").getCostBasisIncludingCommission(date));
    Assert.assertEquals(new BigDecimal(10), userModel.getPortfolio("p3").getValue(date));
    Assert.assertEquals(expectedApplePurchaseComposition, userModel.getPortfolio("p3").toString());

    userModel.buyShares(appleShare.getTickerName(), "p1", date, 1);
    Assert.assertEquals(new BigDecimal(20),
            userModel.getPortfolio("p1").getCostBasisIncludingCommission(date));
    Assert.assertEquals(new BigDecimal(20), userModel.getPortfolio("p1").getValue(date));
    Assert.assertEquals("Buy Date            Stocks              Quantity            Cost Price  " +
            "        Current Value       Commission Percentage\n" +
            "2018-11-01          AAPL                1                   $10.00              $10" +
            ".00              0.0%\n" +
            "2018-11-01          AAPL                1                   $10.00              $10" +
            ".00              0.0%\n" +
            "\n" +
            "Total Value:                                      $20.00\n" +
            "Total Cost (excluding commission):                $20.00\n" +
            "Total Cost (including commission):                $20.00\n" +
            "Profit:                                           $0.00", userModel.getPortfolio("p1"
    ).toString());
  }

  @Test
  public void buyStockOfSameCompanyAcrossMultipleStretches() throws IllegalArgumentException,
          StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");
    Share appleShare = getAppleShare();

    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);

    Date day3 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day2 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day1 = calendar.getTime();

    userModel.buyShares(appleShare.getTickerName(), "p1", day1, 1);
    Assert.assertEquals(new BigDecimal(30),
            userModel.getPortfolio("p1").getCostBasisIncludingCommission(day1));

    userModel.buyShares(appleShare.getTickerName(), "p1", day2, 3);
    Assert.assertEquals(new BigDecimal(90),
            userModel.getPortfolio("p1").getCostBasisIncludingCommission(day2));

    userModel.buyShares(appleShare.getTickerName(), "p1", day3, 5);
    Assert.assertEquals(new BigDecimal(140),
            userModel.getPortfolio("p1").getCostBasisIncludingCommission(day3));
    Assert.assertEquals("Buy Date            Stocks              Quantity            Cost Price  " +
            "        Current Value       Commission Percentage\n" +
            "2018-10-30          AAPL                1                   $30.00              $10" +
            ".00              0.0%\n" +
            "2018-10-31          AAPL                3                   $20.00              $10" +
            ".00              0.0%\n" +
            "2018-11-01          AAPL                5                   $10.00              $10" +
            ".00              0.0%\n" +
            "\n" +
            "Total Value:                                      $90.00\n" +
            "Total Cost (excluding commission):                $140.00\n" +
            "Total Cost (including commission):                $140.00\n" +
            "Profit:                                           ($50.00)", userModel.getPortfolio(
            "p1").toString());
  }

  @Test
  public void buyStockOfDifferentCompanyAcrossMultipleStretches() throws IllegalArgumentException,
          StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");
    userModel.createPortfolio("p2");
    Share appleShare = getAppleShare();
    Share googleShare = getGoogleShare();

    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);

    Date day3 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day2 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day1 = calendar.getTime();

    userModel.buyShares(appleShare.getTickerName(), "p1", day1, 1);
    Assert.assertEquals(new BigDecimal(30),
            userModel.getPortfolio("p1").getCostBasisIncludingCommission(day1));

    userModel.buyShares(appleShare.getTickerName(), "p1", day2, 3);
    Assert.assertEquals(new BigDecimal(90),
            userModel.getPortfolio("p1").getCostBasisIncludingCommission(day2));

    userModel.buyShares(appleShare.getTickerName(), "p1", day3, 5);
    Assert.assertEquals(new BigDecimal(140),
            userModel.getPortfolio("p1").getCostBasisIncludingCommission(day3));

    userModel.buyShares(googleShare.getTickerName(), "p1", day1, 1);
    Assert.assertEquals(new BigDecimal(41),
            userModel.getPortfolio("p1").getCostBasisIncludingCommission(day1));

    userModel.buyShares(googleShare.getTickerName(), "p1", day2, 3);
    Assert.assertEquals(new BigDecimal(134),
            userModel.getPortfolio("p1").getCostBasisIncludingCommission(day2));

    userModel.buyShares(googleShare.getTickerName(), "p1", day3, 5);
    Assert.assertEquals(new BigDecimal(239),
            userModel.getPortfolio("p1").getCostBasisIncludingCommission(day3));
    Assert.assertEquals("Buy Date            Stocks              Quantity            Cost Price  " +
            "        Current Value       Commission Percentage\n" +
            "2018-10-30          AAPL                1                   $30.00              $10" +
            ".00              0.0%\n" +
            "2018-10-31          AAPL                3                   $20.00              $10" +
            ".00              0.0%\n" +
            "2018-11-01          AAPL                5                   $10.00              $10" +
            ".00              0.0%\n" +
            "2018-10-30          GOOG                1                   $11.00              $11" +
            ".00              0.0%\n" +
            "2018-10-31          GOOG                3                   $11.00              $11" +
            ".00              0.0%\n" +
            "2018-11-01          GOOG                5                   $11.00              $11" +
            ".00              0.0%\n" +
            "\n" +
            "Total Value:                                      $189.00\n" +
            "Total Cost (excluding commission):                $239.00\n" +
            "Total Cost (including commission):                $239.00\n" +
            "Profit:                                           ($50.00)", userModel.getPortfolio(
            "p1").toString());


    //////// portfolio 2 ///////

    userModel.buyShares(appleShare.getTickerName(), "p2", day1, 1);
    Assert.assertEquals(new BigDecimal(30),
            userModel.getPortfolio("p2").getCostBasisIncludingCommission(day1));

    userModel.buyShares(appleShare.getTickerName(), "p2", day2, 3);
    Assert.assertEquals(new BigDecimal(90),
            userModel.getPortfolio("p2").getCostBasisIncludingCommission(day2));

    userModel.buyShares(appleShare.getTickerName(), "p2", day3, 5);
    Assert.assertEquals(new BigDecimal(140),
            userModel.getPortfolio("p2").getCostBasisIncludingCommission(day3));

    userModel.buyShares(googleShare.getTickerName(), "p2", day1, 1);
    Assert.assertEquals(new BigDecimal(41),
            userModel.getPortfolio("p2").getCostBasisIncludingCommission(day1));

    userModel.buyShares(googleShare.getTickerName(), "p2", day2, 3);
    Assert.assertEquals(new BigDecimal(134),
            userModel.getPortfolio("p2").getCostBasisIncludingCommission(day2));

    userModel.buyShares(googleShare.getTickerName(), "p2", day3, 5);
    Assert.assertEquals(new BigDecimal(239),
            userModel.getPortfolio("p2").getCostBasisIncludingCommission(day3));
    Assert.assertEquals("Buy Date            Stocks              Quantity            Cost Price  " +
            "        Current Value       Commission Percentage\n" +
            "2018-10-30          AAPL                1                   $30.00              $10" +
            ".00              0.0%\n" +
            "2018-10-31          AAPL                3                   $20.00              $10" +
            ".00              0.0%\n" +
            "2018-11-01          AAPL                5                   $10.00              $10" +
            ".00              0.0%\n" +
            "2018-10-30          GOOG                1                   $11.00              $11" +
            ".00              0.0%\n" +
            "2018-10-31          GOOG                3                   $11.00              $11" +
            ".00              0.0%\n" +
            "2018-11-01          GOOG                5                   $11.00              $11" +
            ".00              0.0%\n" +
            "\n" +
            "Total Value:                                      $189.00\n" +
            "Total Cost (excluding commission):                $239.00\n" +
            "Total Cost (including commission):                $239.00\n" +
            "Profit:                                           ($50.00)", userModel.getPortfolio(
            "p2").toString());
  }

  @Test
  public void gettingPortfolioValueWorks() throws StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");

    Share appleShare = getAppleShare();

    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
    Date day3 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day2 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day1 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day0 = calendar.getTime();


    calendar.add(Calendar.YEAR, 100);
    Date after100Years = calendar.getTime();

    userModel.buyShares(appleShare.getTickerName(), "p1", day1, 1);
    userModel.buyShares(appleShare.getTickerName(), "p1", day2, 1);
    userModel.buyShares(appleShare.getTickerName(), "p1", day3, 1);

    BigDecimal dayOneValue = userModel.getPortfolio("p1").getValue(day0);
    Assert.assertEquals(BigDecimal.ZERO, dayOneValue);

    Assert.assertEquals(new BigDecimal("30"),
            userModel.getPortfolio("p1").getValue(day3));

    Assert.assertEquals(new BigDecimal("40"),
            userModel.getPortfolio("p1").getValue(day2));
    try {
      userModel.getPortfolio("p1").getValue(after100Years);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Time cannot be in Future", e.getMessage());
    }

  }

  @Test
  public void gettingPortfolioCostWorks() throws StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");

    Share appleShare = getAppleShare();

    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
    Date day3 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day2 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day1 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day0 = calendar.getTime();


    calendar.add(Calendar.YEAR, 100);
    Date after100Years = calendar.getTime();

    userModel.buyShares(appleShare.getTickerName(), "p1", day1, 1);
    userModel.buyShares(appleShare.getTickerName(), "p1", day2, 1);
    userModel.buyShares(appleShare.getTickerName(), "p1", day3, 1);

    BigDecimal dayOneValue = userModel.getPortfolio("p1").getCostBasisIncludingCommission(day0);
    Assert.assertEquals(BigDecimal.ZERO, dayOneValue);

    Assert.assertEquals(new BigDecimal("60"),
            userModel.getPortfolio("p1").getCostBasisIncludingCommission(day3));

    Assert.assertEquals(new BigDecimal("50"),
            userModel.getPortfolio("p1").getCostBasisIncludingCommission(day2));

    Assert.assertEquals(new BigDecimal("30"),
            userModel.getPortfolio("p1").getCostBasisIncludingCommission(day1));
    try {
      userModel.getPortfolio("p1").getCostBasisIncludingCommission(after100Years);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Time cannot be in Future", e.getMessage());
    }

  }

  @Test
  public void getAllPortfolioNamesWorks() {
    UserModel userModel = TestUtils.getMockedUserModel();

    Assert.assertEquals(Collections.emptyList(), userModel.getAllPortfolios());

    userModel.createPortfolio("p1");
    List<String> actualPortfolioNames =
            userModel.getAllPortfolios().stream()
                    .map(Portfolio::getName).collect(Collectors.toList());
    Assert.assertEquals(Collections.singletonList("p1"), actualPortfolioNames);
    userModel.createPortfolio("p2");
    actualPortfolioNames =
            userModel.getAllPortfolios().stream()
                    .map(Portfolio::getName).collect(Collectors.toList());
    Assert.assertEquals(Arrays.asList("p1", "p2"), actualPortfolioNames);
  }

  @Test
  public void getAllPortfoliosInAPortfolioIsUnmodifiable() {
    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");

    Share appleShare = getAppleShare();

    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
    Date day3 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day2 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day1 = calendar.getTime();

    userModel.buyShares(appleShare.getTickerName(), "p1", day1, 1);
    userModel.buyShares(appleShare.getTickerName(), "p1", day2, 1);
    userModel.buyShares(appleShare.getTickerName(), "p1", day3, 1);

    Portfolio portfolio = userModel.getPortfolio("p1");
    List<SharePurchaseOrder> purchases = portfolio.getPurchases();
    try {
      purchases.add(new SharePurchaseOrder("AAPL", new StockPrice(new BigDecimal(100),
              TestUtils.getValidDateForTrading()), 100));
      Assert.fail("should have failed");
    } catch (UnsupportedOperationException ignored) {

    }
  }

  protected static Share getAppleShare() {
    return new Share("AAPL", BigDecimal.TEN);
  }

  private Share getGoogleShare() {
    return new Share("GOOG", new BigDecimal("11"));
  }

  private static Date getDateAfterClosingTime() {
    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 4, 0);
    return calendar.getTime();
  }

  private static Date getDateBeforeOpeningTime() {
    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 7, 59);
    return calendar.getTime();
  }

  private static Date getWeekendDate() {
    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(2018, Calendar.NOVEMBER, 11, 10, 0);
    return calendar.getTime();
  }

  private UserModel getUserModelWithEmptyPortfolio() {
    UserModel userModel = getUserModelWithSimpleStockDao();
    userModel.createPortfolio("p1");
    return userModel;
  }

}
