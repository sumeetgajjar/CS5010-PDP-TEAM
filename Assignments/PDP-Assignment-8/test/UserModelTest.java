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

    for (int i = 1; i <= 4; i++) {
      String portfolioName = String.format("p%d", i);
      userModel.createPortfolio(portfolioName);
      Portfolio portfolio = userModel.getPortfolio(portfolioName);
      Assert.assertEquals(portfolioName, portfolio.getName());
    }

    Assert.assertEquals(4, userModel.getAllPortfolios().size());
  }

  @Test
  public void addPortfolioWorks() {
    UserModel userModel = getEmptyUserModel();
    userModel.createPortfolio("Hello world");
    Assert.assertEquals("Hello world", userModel.getPortfolio("Hello world").getName());
  }

  @Test
  public void addPortfolioFails() {
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

    userModel.addShareData(new Share("AAPL", BigDecimal.TEN), date);
    userModel.buyShares("AAPL", portfolioName, date, 1);
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


  private Date getValidDateForTrading() {
    Calendar calendar = Calendar.getInstance();
    calendar.set(2018, Calendar.DECEMBER, 1, 10, 0);
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
