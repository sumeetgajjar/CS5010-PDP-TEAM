import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import virtualgambling.model.SimpleUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.bean.Share;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdatasource.SimpleStockExchange;
import virtualgambling.model.stockexchange.SimpleStockDataSource;
import virtualgambling.model.stockexchange.StockDataSource;

/**
 * Created by gajjar.s, on 9:52 PM, 11/12/18
 */
public class UserModelTest {

  private static final BigDecimal DEFAULT_USER_CAPITAL = new BigDecimal("10000000");

  @Test
  public void testInitializationOfUserModel() {
    UserModel userModel = getEmptyUserModel();
    try {
      userModel.getPortfolioComposition("test");
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Portfolio does not exists", e.getMessage());
    }

    userModel.createPortfolio("test");

    Date date = new Date();

    Assert.assertEquals("", userModel.getPortfolioComposition("test"));
    Assert.assertEquals(BigDecimal.ZERO, userModel.getCostBasisOfPortfolio("p1", date));
    Assert.assertEquals(BigDecimal.ZERO, userModel.getPortfolioValue("p1", date));

    Assert.assertEquals(DEFAULT_USER_CAPITAL, userModel.getRemainingCapital());
  }

  @Test
  public void createPortfolioWorks() {
    UserModel userModel = getEmptyUserModel();
    userModel.createPortfolio("Hello world");
    Assert.assertEquals("Hello world", userModel.getPortfolioComposition("Hello world"));
  }

  @Test
  public void createPortfolioFails() {
    UserModel userModel = getEmptyUserModel();
    try {
      userModel.createPortfolio(null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid Portfolio Name", e.getMessage());
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
    UserModel userModel = getMockedDataSourceEmptyUser();
    Date date = getValidDateForTrading();
    Share appleShare = getAppleShare();

    userModel.createPortfolio("p1");
    try {
      userModel.buyShares(appleShare.getTickerName(), "p1", date, 0);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Cannot buy shares at given time", e.getMessage());
    }

    try {
      userModel.buyShares(appleShare.getTickerName(), "p1", date, -1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Cannot buy shares at given time", e.getMessage());
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
      Assert.assertNull("Invalid input", e.getMessage());
    }

    try {
      userModel.buyShares(appleShare.getTickerName(), null, date, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Invalid input", e.getMessage());
    }

    try {
      userModel.buyShares(appleShare.getTickerName(), "p1", null, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Invalid input", e.getMessage());
    }
  }

  @Test
  public void buyingShareForMissingPortfolioFails() throws StockDataNotFoundException {
    UserModel userModel = getEmptyUserModel();
    Date date = getValidDateForTrading();
    Share appleShare = getAppleShare();

    try {
      userModel.buyShares(appleShare.getTickerName(), "p1", date, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Portfolio does not exist", e.getMessage());
    }
  }

  @Test
  public void buyingSharesAtInvalidTimeFails() throws StockDataNotFoundException {
    UserModel userModel = getMockedDataSourceEmptyUser();
    Share appleShare = getAppleShare();

    try {
      Date weekendDate = getWeekendDate();
      userModel.buyShares(appleShare.getTickerName(), "p1", weekendDate, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Cannot buy shares at given time", e.getMessage());
    }

    try {
      Date beforeOpeningTime = getDateBeforeOpeningTime();
      userModel.buyShares(appleShare.getTickerName(), "p1", beforeOpeningTime, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Cannot buy shares at given time", e.getMessage());
    }

    try {
      Date afterClosingTime = getDateAfterClosingTime();
      userModel.buyShares(appleShare.getTickerName(), "p1", afterClosingTime, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Cannot buy shares at given time", e.getMessage());
    }

    try {
      Date futureTime = getFutureTime();
      userModel.buyShares(appleShare.getTickerName(), "p1", futureTime, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Cannot buy shares at given time", e.getMessage());
    }
  }

  @Test
  public void getRemainingCapitalWorks() throws StockDataNotFoundException {
    UserModel userModel = getMockedDataSourceEmptyUser();
    Date date = getValidDateForTrading();
    Share appleShare = getAppleShare();

    Assert.assertEquals(DEFAULT_USER_CAPITAL, userModel.getRemainingCapital());

    userModel.buyShares("AAPL", "p1", date, 1);
    Assert.assertEquals(DEFAULT_USER_CAPITAL.subtract(appleShare.getUnitPrice()),
            userModel.getRemainingCapital());
  }

  @Test
  public void buyingStockWhoseDataIsNotPresentFails() throws StockDataNotFoundException {
    UserModel userModel = getMockedDataSourceEmptyUser();
    try {
      Calendar calendar = Calendar.getInstance();
      calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
      calendar.add(Calendar.DATE, -1);
      Date date = calendar.getTime();

      userModel.buyShares(getAppleShare().getTickerName(), "p1", date, 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Stock Data not found", e.getMessage());
    }
  }

  @Test
  public void buyingFailsForInvalidTickerName() throws StockDataNotFoundException {
    UserModel userModel = getMockedDataSourceEmptyUser();
    try {
      userModel.buyShares("AAPL1", "p1", getValidDateForTrading(), 1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Stock Data not found", e.getMessage());
    }
  }

  @Test
  public void buyFailsDueToInsufficientFunds() throws StockDataNotFoundException {
    UserModel userModel = getUserModelWithEmptyPortfolio();
    Date date = getValidDateForTrading();

    try {
      userModel.buyShares(getAppleShare().getTickerName(), "p1", date,
              DEFAULT_USER_CAPITAL.divide(BigDecimal.TEN, BigDecimal.ROUND_CEILING).longValue() + 1);
      Assert.fail("should have failed");
    } catch (IllegalStateException e) {
      Assert.assertNull("Insufficient funds", e.getMessage());
    }
  }

  @Test
  public void getCostBasisFailsDueToInvalidArguments() throws StockDataNotFoundException {
    UserModel userModel = getMockedDataSourceEmptyUser();
    userModel.createPortfolio("p1");
    Date date = getValidDateForTrading();
    userModel.buyShares(getAppleShare().getTickerName(), "p1", date, 1);

    try {
      userModel.getCostBasisOfPortfolio(null, date);
      Assert.fail("should have failed");
    } catch (IllegalStateException e) {
      Assert.assertNull("Invalid input", e.getMessage());
    }

    try {
      userModel.getCostBasisOfPortfolio("p1", null);
      Assert.fail("should have failed");
    } catch (IllegalStateException e) {
      Assert.assertNull("Invalid input", e.getMessage());
    }
  }

  @Test
  public void getPortfolioValueFailsDueToInvalidArguments() throws StockDataNotFoundException {
    UserModel userModel = getMockedDataSourceEmptyUser();
    userModel.createPortfolio("p1");
    Date date = getValidDateForTrading();
    userModel.buyShares(getAppleShare().getTickerName(), "p1", date, 1);

    try {
      userModel.getPortfolioValue(null, date);
      Assert.fail("should have failed");
    } catch (IllegalStateException e) {
      Assert.assertNull("Invalid input", e.getMessage());
    }

    try {
      userModel.getPortfolioValue("p1", null);
      Assert.fail("should have failed");
    } catch (IllegalStateException e) {
      Assert.assertNull("Invalid input", e.getMessage());
    }
  }

  @Test
  public void buyMultipleStocksWorks() throws IllegalArgumentException, StockDataNotFoundException {
    Share appleShare = getAppleShare();
    Share googleShare = getGoogleShare();

    Date date = getValidDateForTrading();

    UserModel userModel = getMockedDataSourceEmptyUser();
    userModel.createPortfolio("p1");
    userModel.createPortfolio("p2");
    userModel.createPortfolio("p3");

    userModel.buyShares(appleShare.getTickerName(), "p1", date, 1);
    userModel.buyShares(appleShare.getTickerName(), "p3", date, 1);
    userModel.buyShares(googleShare.getTickerName(), "p2", date, 1);

    Assert.assertEquals(new BigDecimal(10), userModel.getCostBasisOfPortfolio("p1", date));
    Assert.assertEquals(new BigDecimal(10), userModel.getPortfolioValue("p1", date));
    Assert.assertEquals("", userModel.getPortfolioComposition("p1"));

    Assert.assertEquals(new BigDecimal(11), userModel.getCostBasisOfPortfolio("p2", date));
    Assert.assertEquals(new BigDecimal(11), userModel.getPortfolioValue("p2", date));
    Assert.assertEquals("", userModel.getPortfolioComposition("p2"));

    Assert.assertEquals(new BigDecimal(10), userModel.getCostBasisOfPortfolio("p3", date));
    Assert.assertEquals(new BigDecimal(10), userModel.getPortfolioValue("p3", date));
    Assert.assertEquals("", userModel.getPortfolioComposition("p3"));

    userModel.buyShares(appleShare.getTickerName(), "p1", date, 1);
    Assert.assertEquals(new BigDecimal(20), userModel.getCostBasisOfPortfolio("p1", date));
    Assert.assertEquals(new BigDecimal(20), userModel.getPortfolioValue("p1", date));
    Assert.assertEquals("", userModel.getPortfolioComposition("p1"));
  }

  @Test
  public void buyStockOfSameCompanyAcrossMultipleStretches() throws IllegalArgumentException,
          StockDataNotFoundException {
    UserModel userModel = getUserModelWithEmptyPortfolio();
    Share appleShare = getAppleShare();

    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);

    Date day3 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day2 = calendar.getTime();

    calendar.add(Calendar.DATE, -2);
    Date day1 = calendar.getTime();

    userModel.buyShares(appleShare.getTickerName(), "p1", day1, 1);
    Assert.assertEquals("", userModel.getPortfolioComposition("p1"));
    Assert.assertEquals(new BigDecimal(30), userModel.getCostBasisOfPortfolio("p1", day1));

    userModel.buyShares(appleShare.getTickerName(), "p1", day2, 3);
    Assert.assertEquals("", userModel.getPortfolioComposition("p1"));
    Assert.assertEquals(new BigDecimal(90), userModel.getCostBasisOfPortfolio("p1", day2));

    userModel.buyShares(appleShare.getTickerName(), "p1", day3, 5);
    Assert.assertEquals("", userModel.getPortfolioComposition("p1"));
    Assert.assertEquals(new BigDecimal(140), userModel.getCostBasisOfPortfolio("p1", day3));
  }

  @Test
  public void gettingPortfolioValueWorks() throws StockDataNotFoundException {
    UserModel userModel = getMockedDataSourceEmptyUser();
    userModel.createPortfolio("p1");

    Share appleShare = getAppleShare();

    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
    Date day3 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day2 = calendar.getTime();

    calendar.add(Calendar.DATE, -2);
    Date day1 = calendar.getTime();

    calendar.add(Calendar.DATE, -3);
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
      Assert.assertEquals("Invalid input", e.getMessage());
    }

  }

  @Test
  public void gettingPortfolioCostWorks() throws StockDataNotFoundException {
    UserModel userModel = getMockedDataSourceEmptyUser();
    userModel.createPortfolio("p1");

    Share appleShare = getAppleShare();

    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
    Date day3 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day2 = calendar.getTime();

    calendar.add(Calendar.DATE, -2);
    Date day1 = calendar.getTime();

    calendar.add(Calendar.DATE, -3);
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
            userModel.getCostBasisOfPortfolio("p1", day2));
    try {
      userModel.getCostBasisOfPortfolio("p1", after100Years);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

  }

  private Share getAppleShare() {
    return new Share("AAPL", BigDecimal.TEN);
  }

  private Share getGoogleShare() {
    return new Share("GOOG", new BigDecimal("11"));
  }

  private Date getFutureTime() {
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, 1);
    return calendar.getTime();
  }

  private Date getDateAfterClosingTime() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 4, 1);
    return calendar.getTime();
  }

  private Date getDateBeforeOpeningTime() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 8, 59);
    return calendar.getTime();
  }

  private Date getWeekendDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 11, 10, 0);
    return calendar.getTime();
  }

  private static Date getValidDateForTrading() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
    return calendar.getTime();
  }

  private UserModel getUserModelWithEmptyPortfolio() {
    UserModel userModel = getEmptyUserModel();
    userModel.createPortfolio("p1");
    return userModel;
  }

  private UserModel getEmptyUserModel() {
    return new SimpleUserModel(new SimpleStockExchange(new SimpleStockDataSource()));
  }

  private UserModel getMockedDataSourceEmptyUser() {
    return new SimpleUserModel(new SimpleStockExchange(new MockDataSource()));
  }

  private static class MockDataSource implements StockDataSource {
    @Override
    public BigDecimal getPrice(String tickerName, Date date) throws StockDataNotFoundException {
      if (tickerName.equals("AAPL")) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
        Date day3 = calendar.getTime();
        if (date.equals(day3)) {
          return BigDecimal.TEN;
        }

        calendar.add(Calendar.DATE, -1);
        Date day2 = calendar.getTime();
        if (date.equals(day2)) {
          return new BigDecimal(20);
        }

        calendar.add(Calendar.DATE, -2);
        Date day1 = calendar.getTime();
        if (date.equals(day1)) {
          return new BigDecimal(30);
        }

      } else if (tickerName.equals("GOOG")) {
        return new BigDecimal("11");
      }

      throw new StockDataNotFoundException();
    }
  }
}
