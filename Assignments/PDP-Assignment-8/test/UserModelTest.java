import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import util.Share;
import util.TestUtils;
import virtualgambling.model.UserModel;
import virtualgambling.model.exceptions.InsufficientCapitalException;
import virtualgambling.model.exceptions.StockDataNotFoundException;

/**
 * Created by gajjar.s, on 9:52 PM, 11/12/18
 */
public class UserModelTest {

  @Test
  public void testInitializationOfUserModel() {
    UserModel userModel = TestUtils.getEmptyUserModel();
    try {
      userModel.getPortfolioComposition("test");
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Portfolio not found", e.getMessage());
    }

    userModel.createPortfolio("test");

    Date date = new Date();

    Assert.assertEquals("Buy Date            Stocks              Cost Price          Current "
            + "Value\n"
            + "\n"
            + "Total Value:        $0.00\n"
            + "Total Cost:         $0.00\n"
            + "Profit:             $0.00", userModel.getPortfolioComposition("test"));
    Assert.assertEquals(BigDecimal.ZERO, userModel.getCostBasisOfPortfolio("test", date));
    Assert.assertEquals(BigDecimal.ZERO, userModel.getPortfolioValue("test", date));

    Assert.assertEquals(TestUtils.DEFAULT_USER_CAPITAL, userModel.getRemainingCapital());
  }

  @Test
  public void createPortfolioWorks() {
    UserModel userModel = TestUtils.getEmptyUserModel();
    userModel.createPortfolio("Hello world");
    Assert.assertEquals("Buy Date            Stocks              Cost Price          Current "
            + "Value\n"
            + "\n"
            + "Total Value:        $0.00\n"
            + "Total Cost:         $0.00\n"
            + "Profit:             $0.00", userModel.getPortfolioComposition("Hello world"));
  }

  @Test
  public void createPortfolioFails() {
    UserModel userModel = TestUtils.getEmptyUserModel();
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

    userModel.createPortfolio("p1");
    try {
      userModel.createPortfolio("p1");
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Portfolio already exists", e.getMessage());
    }
  }

  @Test
  public void buyingSharesOfInvalidQuantityFails() throws IllegalArgumentException,
          StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    Date date = getValidDateForTrading();
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

  @Test
  public void buyingShareFailsForNullInputs() throws StockDataNotFoundException {
    UserModel userModel = getUserModelWithEmptyPortfolio();
    Date date = getValidDateForTrading();
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
    UserModel userModel = TestUtils.getEmptyUserModel();
    Date date = getValidDateForTrading();
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
    UserModel userModel = TestUtils.getMockedUserModel();
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
      Date futureTime = getFutureTime();
      userModel.buyShares(appleShare.getTickerName(), "p1", futureTime, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Time cannot be in Future", e.getMessage());
    }
  }

  @Test
  public void getRemainingCapitalWorks() throws StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    Date date = getValidDateForTrading();
    Share appleShare = getAppleShare();
    userModel.createPortfolio("p1");

    Assert.assertEquals(TestUtils.DEFAULT_USER_CAPITAL, userModel.getRemainingCapital());

    userModel.buyShares("AAPL", "p1", date, 1);
    Assert.assertEquals(TestUtils.DEFAULT_USER_CAPITAL.subtract(appleShare.getUnitPrice()),
            userModel.getRemainingCapital());
  }

  @Test
  public void buyingStockWhoseDataIsNotPresentFails() throws StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");
    try {
      Calendar calendar = Calendar.getInstance();
      calendar.set(2018, Calendar.JULY, 4, 10, 0);
      calendar.add(Calendar.DATE, -1);
      Date date = calendar.getTime();

      userModel.buyShares(getAppleShare().getTickerName(), "p1", date, 1);
      Assert.fail("should have failed");
    } catch (StockDataNotFoundException e) {
      Assert.assertEquals("Stock Data not found", e.getMessage());
    }
  }

  @Test
  public void buyingFailsForInvalidTickerName() throws StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");
    try {
      userModel.buyShares("AAPL1", "p1", getValidDateForTrading(), 1);
      Assert.fail("should have failed");
    } catch (StockDataNotFoundException e) {
      Assert.assertEquals("Stock Data not found", e.getMessage());
    }
  }

  @Test
  public void buyFailsDueToInsufficientFunds() throws StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    Date date = getValidDateForTrading();
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
    Date date = getValidDateForTrading();
    userModel.buyShares(getAppleShare().getTickerName(), "p1", date, 1);

    try {
      userModel.getCostBasisOfPortfolio(null, date);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      userModel.getCostBasisOfPortfolio("p1", null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
  }

  @Test
  public void getPortfolioValueFailsDueToInvalidArguments() throws StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");
    Date date = getValidDateForTrading();
    userModel.buyShares(getAppleShare().getTickerName(), "p1", date, 1);

    try {
      userModel.getPortfolioValue(null, date);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      userModel.getPortfolioValue("p1", null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
  }

  @Test
  public void buyMultipleStocksWorks() throws IllegalArgumentException, StockDataNotFoundException {
    Share appleShare = getAppleShare();
    Share googleShare = getGoogleShare();

    Date date = getValidDateForTrading();

    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");
    userModel.createPortfolio("p2");
    userModel.createPortfolio("p3");

    userModel.buyShares(appleShare.getTickerName(), "p1", date, 1);
    userModel.buyShares(appleShare.getTickerName(), "p3", date, 1);
    userModel.buyShares(googleShare.getTickerName(), "p2", date, 1);

    Assert.assertEquals(new BigDecimal(10), userModel.getCostBasisOfPortfolio("p1", date));
    Assert.assertEquals(new BigDecimal(10), userModel.getPortfolioValue("p1", date));
    String expectedApplePurchaseComposition = "Buy Date            Stocks              Cost Price"
            + "          Current "
            + "Value\n"
            + "2018-11-01          AAPL                $10.00              $10.00\n"
            + "\n"
            + "Total Value:        $10.00\n"
            + "Total Cost:         $10.00\n"
            + "Profit:             $0.00";

    String expectedGooglePurchase = "Buy Date            Stocks              Cost Price          "
            + "Current Value\n"
            + "2018-11-01          GOOG                $11.00              $11.00\n"
            + "\n"
            + "Total Value:        $11.00\n"
            + "Total Cost:         $11.00\n"
            + "Profit:             $0.00";
    Assert.assertEquals(expectedApplePurchaseComposition, userModel.getPortfolioComposition("p1"));

    Assert.assertEquals(new BigDecimal(11), userModel.getCostBasisOfPortfolio("p2", date));
    Assert.assertEquals(new BigDecimal(11), userModel.getPortfolioValue("p2", date));
    Assert.assertEquals(expectedGooglePurchase, userModel.getPortfolioComposition("p2"));

    Assert.assertEquals(new BigDecimal(10), userModel.getCostBasisOfPortfolio("p3", date));
    Assert.assertEquals(new BigDecimal(10), userModel.getPortfolioValue("p3", date));
    Assert.assertEquals(expectedApplePurchaseComposition, userModel.getPortfolioComposition("p3"));

    userModel.buyShares(appleShare.getTickerName(), "p1", date, 1);
    Assert.assertEquals(new BigDecimal(20), userModel.getCostBasisOfPortfolio("p1", date));
    Assert.assertEquals(new BigDecimal(20), userModel.getPortfolioValue("p1", date));
    Assert.assertEquals("Buy Date            Stocks              Cost Price          Current "
            + "Value\n"
            + "2018-11-01          AAPL                $10.00              $10.00\n"
            + "2018-11-01          AAPL                $10.00              $10.00\n"
            + "\n"
            + "Total Value:        $20.00\n"
            + "Total Cost:         $20.00\n"
            + "Profit:             $0.00", userModel.getPortfolioComposition("p1"));
  }

  @Test
  public void buyStockOfSameCompanyAcrossMultipleStretches() throws IllegalArgumentException,
          StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");
    Share appleShare = getAppleShare();

    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);

    Date day3 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day2 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day1 = calendar.getTime();

    userModel.buyShares(appleShare.getTickerName(), "p1", day1, 1);
    Assert.assertEquals(new BigDecimal(30), userModel.getCostBasisOfPortfolio("p1", day1));

    userModel.buyShares(appleShare.getTickerName(), "p1", day2, 3);
    Assert.assertEquals(new BigDecimal(90), userModel.getCostBasisOfPortfolio("p1", day2));

    userModel.buyShares(appleShare.getTickerName(), "p1", day3, 5);
    Assert.assertEquals(new BigDecimal(140), userModel.getCostBasisOfPortfolio("p1", day3));
    Assert.assertEquals("Buy Date            Stocks              Cost Price          Current "
            + "Value\n"
            + "2018-10-30          AAPL                $30.00              $10.00\n"
            + "2018-10-31          AAPL                $20.00              $10.00\n"
            + "2018-11-01          AAPL                $10.00              $10.00\n"
            + "\n"
            + "Total Value:        $90.00\n"
            + "Total Cost:         $140.00\n"
            + "Profit:             ($50.00)", userModel.getPortfolioComposition("p1"));
  }

  @Test
  public void gettingPortfolioValueWorks() throws StockDataNotFoundException {
    UserModel userModel = TestUtils.getMockedUserModel();
    userModel.createPortfolio("p1");

    Share appleShare = getAppleShare();

    Calendar calendar = Calendar.getInstance();
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

    BigDecimal dayOneValue = userModel.getPortfolioValue("p1", day0);
    Assert.assertEquals(BigDecimal.ZERO, dayOneValue);

    Assert.assertEquals(new BigDecimal("30"),
            userModel.getPortfolioValue("p1", day3));

    Assert.assertEquals(new BigDecimal("40"),
            userModel.getPortfolioValue("p1", day2));
    try {
      userModel.getPortfolioValue("p1", after100Years);
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

    Calendar calendar = Calendar.getInstance();
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

    BigDecimal dayOneValue = userModel.getCostBasisOfPortfolio("p1", day0);
    Assert.assertEquals(BigDecimal.ZERO, dayOneValue);

    Assert.assertEquals(new BigDecimal("60"),
            userModel.getCostBasisOfPortfolio("p1", day3));

    Assert.assertEquals(new BigDecimal("50"),
            userModel.getCostBasisOfPortfolio("p1", day2));

    Assert.assertEquals(new BigDecimal("30"),
            userModel.getCostBasisOfPortfolio("p1", day1));
    try {
      userModel.getCostBasisOfPortfolio("p1", after100Years);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Time cannot be in Future", e.getMessage());
    }

  }

  @Test
  public void getAllPortfolioNamesWorks() {
    UserModel userModel = TestUtils.getMockedUserModel();

    Assert.assertEquals("", userModel.getAllPortfolioNames());

    userModel.createPortfolio("p1");
    Assert.assertEquals("p1", userModel.getAllPortfolioNames());
    userModel.createPortfolio("p2");
    Assert.assertEquals("p1" + System.lineSeparator() + "p2", userModel.getAllPortfolioNames());
  }

  private Share getAppleShare() {
    return new Share("AAPL", BigDecimal.TEN);
  }

  private Share getGoogleShare() {
    return new Share("GOOG", new BigDecimal("11"));
  }

  private static Date getFutureTime() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, 1);
    return calendar.getTime();
  }

  private static Date getDateAfterClosingTime() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 4, 0);
    return calendar.getTime();
  }

  private static Date getDateBeforeOpeningTime() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 7, 59);
    return calendar.getTime();
  }

  private static Date getWeekendDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 11, 10, 0);
    return calendar.getTime();
  }

  private static Date getValidDateForTrading() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
    return calendar.getTime();
  }

  private static UserModel getUserModelWithEmptyPortfolio() {
    UserModel userModel = TestUtils.getEmptyUserModel();
    userModel.createPortfolio("p1");
    return userModel;
  }

}
