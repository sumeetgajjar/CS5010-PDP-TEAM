import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import virtualgambling.model.SimpleUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.bean.Portfolio;
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
  public void addShareDataFailsForAddingSharesAtInvalidTime() {
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
  }

  @Test
  public void getRemainingCapitalWorks() {
    UserModel userModel = getUserModelWithEmptyPortfolios();
    Date date = getValidDateForTrading();
    BigDecimal appleStockCost = BigDecimal.TEN;
    userModel.addShareData(new Share("AAPL", appleStockCost), date);

    Assert.assertEquals(DEFAULT_USER_CAPITAL, userModel.getRemainingCapital());

    userModel.buyShares("AAPL", "p1", date, 1);
    Assert.assertEquals(DEFAULT_USER_CAPITAL.subtract(appleStockCost),
            userModel.getRemainingCapital());
  }

  @Test
  public void buyingStockWhoseDataIsNotPresentFails() {

  }

  private Share getAppleShare() {
    return new Share("AAPL", BigDecimal.TEN);
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

  private UserModel getUserModelWithEmptyPortfolios() {
    UserModel userModel =
            new SimpleUserModel(
                    new SimpleStockExchange(
                            new SimpleStockDataSource()));

    userModel.createPortfolio("p1");
    return userModel;
  }

  private UserModel getEmptyUserModel() {
    return new SimpleUserModel(new SimpleStockExchange(new SimpleStockDataSource()));
  }
}
