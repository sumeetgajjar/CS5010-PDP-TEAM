import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import virtualgambling.model.SimpleUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.PurchaseInfo;
import virtualgambling.model.bean.Share;
import virtualgambling.model.stockdatasource.SimpleStockExchange;
import virtualgambling.model.stockexchange.SimpleStockDataSource;

/**
 * Created by gajjar.s, on 9:52 PM, 11/12/18
 */
public class UserModelTest {

  private static final BigDecimal DEFAULT_USER_CAPITAL = new BigDecimal("10000000");

  @Test
  public void testInitializationOfUserModel() {
    UserModel userModel = getEmptyUserModel();
    Assert.assertEquals(0, userModel.getAllPortfolios().size());
    Assert.assertNull(userModel.getPortfolio("test"));

    userModel.createPortfolio("test");
    Portfolio portfolio = userModel.getPortfolio("test");

    Assert.assertNotNull(portfolio);
    Assert.assertEquals("test", portfolio.getName());
    Assert.assertEquals(0, portfolio.getPurchases().size());
    Assert.assertEquals(BigDecimal.ZERO, portfolio.getCostBasis());
    Assert.assertEquals(BigDecimal.ZERO, portfolio.getPortfolioValue());

    Assert.assertEquals(DEFAULT_USER_CAPITAL, userModel.getRemainingCapital());
  }

  @Test
  public void getPortfolioAndGetAllPortfolioWorks() {
    UserModel userModel = getEmptyUserModel();

    Assert.assertNull(userModel.getPortfolio(" "));
    Assert.assertNull(userModel.getPortfolio(""));
    Assert.assertNull(userModel.getPortfolio(null));

    for (int i = 1; i <= 4; i++) {
      String portfolioName = String.format("p%d", i);

      Assert.assertNull(userModel.getPortfolio(portfolioName));

      userModel.createPortfolio(portfolioName);
      Portfolio portfolio = userModel.getPortfolio(portfolioName);
      Assert.assertEquals(portfolioName, portfolio.getName());
    }

    Assert.assertEquals(4, userModel.getAllPortfolios().size());
  }

  @Test
  public void createPortfolioWorks() {
    UserModel userModel = getEmptyUserModel();
    userModel.createPortfolio("Hello world");
    Assert.assertEquals("Hello world", userModel.getPortfolio("Hello world").getName());
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
  public void addShareDataWorks() {
    UserModel userModel = getEmptyUserModel();
    String portfolioName = "p1";
    userModel.createPortfolio(portfolioName);
    Date date = getValidDateForTrading();

    try {
      userModel.buyShares("AAPL", portfolioName, date, 1);
      Assert.fail("should have failed");
      //todo change exception
    } catch (Exception e) {
      Assert.assertEquals("stock price not found", e.getMessage());
    }

    userModel.addShareData(getAppleShare(), date);
    userModel.buyShares("AAPL", portfolioName, date, 1);
  }

  @Test
  public void addShareDataFails() {
    UserModel userModel = getEmptyUserModel();
    try {
      userModel.addShareData(null, getValidDateForTrading());
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Invalid Input", e.getMessage());
    }

    try {
      userModel.addShareData(getAppleShare(), null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Invalid Input", e.getMessage());
    }

    try {
      userModel.addShareData(null, null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Invalid Input", e.getMessage());
    }
  }

  @Test
  public void addingSharesAtInvalidTimeFails() {
    UserModel userModel = getEmptyUserModel();

    try {
      Date weekendDate = getWeekendDate();
      userModel.addShareData(getAppleShare(), weekendDate);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Invalid Input", e.getMessage());
    }

    try {
      Date beforeOpeningTime = getDateBeforeOpeningTime();
      userModel.addShareData(getAppleShare(), beforeOpeningTime);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Invalid Input", e.getMessage());
    }

    try {
      Date afterClosingTime = getDateAfterClosingTime();
      userModel.addShareData(getAppleShare(), afterClosingTime);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Invalid Input", e.getMessage());
    }

    try {
      Date futureTime = getFutureTime();
      userModel.addShareData(getAppleShare(), futureTime);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertNull("Invalid Input", e.getMessage());
    }
  }

  @Test
  public void buyingSharesOfInvalidQuantityFails() {
    UserModel userModel = getUserModelWithAppleShareDataAdded();
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
  public void buyingShareFailsForNullInputs() {
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
  public void buyingShareForMissingPortfolioFails() {
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
  public void buyingSharesAtInvalidTimeFails() {
    UserModel userModel = getUserModelWithAppleShareDataAdded();
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
  public void getRemainingCapitalWorks() {
    UserModel userModel = getUserModelWithEmptyPortfolio();
    Date date = getValidDateForTrading();
    Share appleShare = getAppleShare();
    userModel.addShareData(appleShare, date);

    Assert.assertEquals(DEFAULT_USER_CAPITAL, userModel.getRemainingCapital());

    userModel.buyShares("AAPL", "p1", date, 1);
    Assert.assertEquals(DEFAULT_USER_CAPITAL.subtract(appleShare.getUnitPrice()),
            userModel.getRemainingCapital());
  }

  @Test
  public void buyingStockWhoseDataIsNotPresentFails() {
    UserModel userModel = getUserModelWithAppleShareDataAdded();
    try {
      Calendar calendar = Calendar.getInstance();
      calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
      calendar.add(Calendar.DATE, -1);
      Date date = calendar.getTime();

      userModel.buyShares(getAppleShare().getTickerName(), "p1", date, 1);
      Assert.fail("should have failed");
      //todo change this
    } catch (Exception e) {
      Assert.assertNull("Stock Data not found", e.getMessage());
    }
  }

  @Test
  public void buyingFailsForInvalidTickerName() {
    UserModel userModel = getUserModelWithAppleShareDataAdded();
    try {
      userModel.buyShares("AAPL1", "p1", getValidDateForTrading(), 1);
      Assert.fail("should have failed");
      //todo change this
    } catch (Exception e) {
      Assert.assertNull("Stock Data not found", e.getMessage());
    }
  }

  @Test
  public void buyFailsDueToInsufficientFunds() {
    UserModel userModel = getUserModelWithEmptyPortfolio();
    Date date = getValidDateForTrading();
    Share appleShare = new Share("AAPL", DEFAULT_USER_CAPITAL.add(BigDecimal.ONE));
    userModel.addShareData(appleShare, date);

    try {
      userModel.buyShares(appleShare.getTickerName(), "p1", date, 1);
      Assert.fail("should have failed");
      //todo change this
    } catch (Exception e) {
      Assert.assertNull("Insufficient funds", e.getMessage());
    }
  }

  @Test
  public void buyStockOfSameCompanyAcrossMultipleStretches() {
    UserModel userModel = getUserModelWithEmptyPortfolio();
    Share appleShare = getAppleShare();

    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);

    Date day3 = calendar.getTime();
    Share appleShareDay3 = new Share("AAPL", new BigDecimal(10));
    userModel.addShareData(appleShareDay3, day3);

    calendar.add(Calendar.DATE, -1);
    Date day2 = calendar.getTime();
    Share appleShareDay2 = new Share("AAPL", new BigDecimal(20));
    userModel.addShareData(appleShareDay2, day2);

    calendar.add(Calendar.DATE, -2);
    Date day1 = calendar.getTime();
    Share appleShareDay1 = new Share("AAPL", new BigDecimal(30));
    userModel.addShareData(appleShareDay1, day1);

    userModel.buyShares(appleShare.getTickerName(), "p1", day1, 1);
    Portfolio portfolio1 = userModel.getPortfolio("p1");
    List<PurchaseInfo> portfolio1Purchases = portfolio1.getPurchases();

    PurchaseInfo applePurchasePortfolio1Day1 = portfolio1Purchases.get(0);
    Assert.assertEquals(appleShareDay1, applePurchasePortfolio1Day1.getShare());
    Assert.assertEquals(1, applePurchasePortfolio1Day1.getQuantity());
    Assert.assertEquals(day1, applePurchasePortfolio1Day1.getDate());

    userModel.buyShares(appleShare.getTickerName(), "p1", day2, 3);
    portfolio1 = userModel.getPortfolio("p1");
    portfolio1Purchases = portfolio1.getPurchases();

    PurchaseInfo applePurchasePortfolio1Day2 = portfolio1Purchases.get(1);
    Assert.assertEquals(appleShareDay2, applePurchasePortfolio1Day2.getShare());
    Assert.assertEquals(3, applePurchasePortfolio1Day2.getQuantity());
    Assert.assertEquals(day2, applePurchasePortfolio1Day2.getDate());

    userModel.buyShares(appleShare.getTickerName(), "p1", day3, 5);
    portfolio1 = userModel.getPortfolio("p1");
    portfolio1Purchases = portfolio1.getPurchases();

    PurchaseInfo applePurchasePortfolio1Day3 = portfolio1Purchases.get(2);
    Assert.assertEquals(appleShareDay3, applePurchasePortfolio1Day3.getShare());
    Assert.assertEquals(5, applePurchasePortfolio1Day3.getQuantity());
    Assert.assertEquals(day2, applePurchasePortfolio1Day2.getDate());
  }

  private Share getAppleShare() {
    return new Share("AAPL", BigDecimal.TEN);
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

  private Date getValidDateForTrading() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);
    return calendar.getTime();
  }

  private UserModel getUserModelWithAppleShareDataAdded() {
    UserModel userModel = getEmptyUserModel();
    Date date = getValidDateForTrading();
    Share appleShare = getAppleShare();
    userModel.addShareData(appleShare, date);
    return userModel;
  }

  private UserModel getUserModelWithEmptyPortfolio() {
    UserModel userModel = getEmptyUserModel();
    userModel.createPortfolio("p1");
    return userModel;
  }

  private UserModel getEmptyUserModel() {
    return new SimpleUserModel(new SimpleStockExchange(new SimpleStockDataSource()));
  }
}
