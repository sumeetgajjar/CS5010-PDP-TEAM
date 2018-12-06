import com.google.gson.reflect.TypeToken;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import util.Constants;
import util.TestUtils;
import util.Utils;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.factory.StockDAOType;
import virtualgambling.model.factory.StockDataSourceType;
import virtualgambling.model.persistence.PortfolioLoader;
import virtualgambling.model.persistence.PortfolioPersister;
import virtualgambling.model.persistence.StrategyLoader;
import virtualgambling.model.persistence.StrategyPersister;
import virtualgambling.model.persistence.serdes.JSONSerDes;
import virtualgambling.model.strategy.RecurringWeightedInvestmentStrategy;
import virtualgambling.model.strategy.Strategy;

public class PersistableUserModelImplTest extends EnhancedUserModelTest {
  private static final String PORTFOLIO_P1 = "p1";
  private static final String PORTFOLIO_FANG = "FANG";

  @Override
  protected UserModel getUserModel(StockDAOType stockDAOType,
                                   StockDataSourceType stockDataSourceType) {
    return TestUtils.getEmptyPersistableUserModelWithStockDAO(stockDAOType, stockDataSourceType);
  }

  @Test
  public void persistPortfolioTest() throws IOException {
    PersistableUserModel userModel = TestUtils.getEmptyPersistableUserModel();

    userModel.createPortfolio(PORTFOLIO_P1);

    Path test = Utils.getPathInDefaultFolder(Paths.get(PORTFOLIO_P1 + ".json"));
    JSONSerDes<Portfolio> serDes = new JSONSerDes<>(test, new TypeToken<Portfolio>() {
    }.getType());

    userModel.buyShares("AAPL", PORTFOLIO_P1, TestUtils.getValidDateForTrading(), 10);

    Portfolio portfolio = userModel.getPortfolio(PORTFOLIO_P1);

    try {
      userModel.persistFromModel(new PortfolioPersister(serDes,
              portfolio));
    } catch (IOException e) {
      Assert.fail();
    }

    try {
      userModel.loadIntoModel(new PortfolioLoader(userModel, serDes));
    } catch (IOException e) {
      Assert.fail();
    }

    Assert.assertEquals(userModel.getPortfolio(PORTFOLIO_P1), portfolio);

    userModel.buyShares("GOOG",
            PORTFOLIO_P1,
            TestUtils.getValidDateForTrading(),
            10);

    Assert.assertNotEquals(userModel.getPortfolio(PORTFOLIO_P1), portfolio);

    try {
      userModel.loadIntoModel(new PortfolioLoader(userModel, serDes));
    } catch (IOException e) {
      Assert.fail();
    }

    Assert.assertEquals(userModel.getPortfolio(PORTFOLIO_P1), portfolio);
  }

  @Test
  public void loadingPortfolioFromFileWorks() throws IOException {
    PersistableUserModel userModel = TestUtils.getEmptyPersistableUserModel();

    userModel.createPortfolio("p2");

    userModel.buyShares("GOOG", "p2", TestUtils.getValidDateForTrading(), 10);

    Portfolio portfolio = userModel.getPortfolio("p2");
    Path test = Utils.getPathInDefaultFolder(Paths.get("p2" + ".json"));
    JSONSerDes<Portfolio> serDes = new JSONSerDes<>(test, new TypeToken<Portfolio>() {
    }.getType());

    try {
      userModel.persistFromModel(new PortfolioPersister(serDes,
              portfolio));
    } catch (Exception e) {
      Assert.fail();
    }

    //reinitializing
    userModel = TestUtils.getEmptyPersistableUserModel();

    try {
      userModel.loadIntoModel(new PortfolioLoader(userModel, serDes));
    } catch (Exception e) {
      Assert.fail();
    }

    Assert.assertEquals("p2", userModel.getPortfolio("p2").getName());
    Assert.assertEquals(1, userModel.getPortfolio("p2").getPurchases().size());
  }

  @Test
  public void serializationOfStrategyWorks() throws IOException {
    PersistableUserModel userModel = TestUtils.getEmptyPersistableUserModel();

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("AAPL", 50.0D);
    stocksWeights.put("GOOG", 50.0D);

    Calendar startCalendar = Utils.getCalendarInstance();
    Calendar endCalendar = Utils.getCalendarInstance();

    startCalendar.set(2018, Calendar.SEPTEMBER, 24);
    endCalendar.set(2018, Calendar.NOVEMBER, 27);
    Strategy recurringWeightedInvestmentStrategy =
            new RecurringWeightedInvestmentStrategy(startCalendar.getTime(),
                    stocksWeights,
                    30,
                    endCalendar.getTime());

    Path test = Utils.getPathInDefaultFolder(Paths.get("strategy1" + ".json"));
    JSONSerDes<Strategy> serDes = new JSONSerDes<>(test,
            new TypeToken<RecurringWeightedInvestmentStrategy>() {
            }.getType());

    try {
      userModel.persistFromModel(new StrategyPersister(serDes,
              recurringWeightedInvestmentStrategy));
    } catch (Exception e) {
      Assert.fail();
    }

    Assert.assertTrue(userModel.getAllPortfolios().isEmpty());

    try {
      userModel.loadIntoModel(new StrategyLoader(userModel, serDes, PORTFOLIO_P1,
              new BigDecimal(1000), 10));
    } catch (Exception e) {
      Assert.fail(e.getMessage());
    }

    Assert.assertFalse(userModel.getAllPortfolios().isEmpty());
    Assert.assertEquals(userModel.getPortfolio(PORTFOLIO_P1).getValue(Utils.getTodayDate()),
            new BigDecimal(110990));
  }

  @Test
  public void invalidInputToPersisterOrLoaderForPortfolioFail() throws IOException {
    PersistableUserModel userModel = TestUtils.getEmptyPersistableUserModel();

    userModel.createPortfolio(PORTFOLIO_P1);

    Path test = null;
    try {
      JSONSerDes<Portfolio> serDes = new JSONSerDes<>(test, new TypeToken<Portfolio>() {
      }.getType());
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals(Constants.INVALID_INPUT, e.getMessage());
    }

    userModel.buyShares("AAPL", PORTFOLIO_P1, TestUtils.getValidDateForTrading(), 10);

    Portfolio portfolio = userModel.getPortfolio(PORTFOLIO_P1);

    test = Utils.getPathInDefaultFolder(Paths.get("p1" + ".json"));

    try {
      JSONSerDes<Portfolio> serDes = new JSONSerDes<>(test, null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals(Constants.INVALID_INPUT, e.getMessage());
    }

    JSONSerDes<Portfolio> serDes = new JSONSerDes<>(test, new TypeToken<Portfolio>() {
    }.getType());

    try {
      userModel.persistFromModel(new PortfolioPersister(null, portfolio));
      Assert.fail("should have failed");
    } catch (Exception e) {
      Assert.assertEquals(Constants.INVALID_INPUT, e.getMessage());
    }

    try {
      userModel.persistFromModel(new PortfolioPersister(serDes, null));
      Assert.fail("should have failed");
    } catch (Exception e) {
      Assert.assertEquals(Constants.INVALID_INPUT, e.getMessage());
    }

    try {
      userModel.persistFromModel(new PortfolioPersister(serDes, portfolio));
    } catch (Exception e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void invalidInputToPersisterOrLoaderForStrategyoFail() throws IOException {
    PersistableUserModel userModel = TestUtils.getEmptyPersistableUserModel();

    Path test = null;
    try {
      JSONSerDes<Strategy> serDes = new JSONSerDes<>(test, new TypeToken<Strategy>() {
      }.getType());
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals(Constants.INVALID_INPUT, e.getMessage());
    }

    test = Utils.getPathInDefaultFolder(Paths.get("p1" + ".json"));

    try {
      JSONSerDes<Portfolio> serDes = new JSONSerDes<>(test, null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals(Constants.INVALID_INPUT, e.getMessage());
    }

    JSONSerDes<Strategy> serDes = new JSONSerDes<>(test, new TypeToken<Strategy>() {
    }.getType());

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("AAPL", 50.0D);
    stocksWeights.put("GOOG", 50.0D);

    Calendar startCalendar = Utils.getCalendarInstance();
    Calendar endCalendar = Utils.getCalendarInstance();

    startCalendar.set(2018, Calendar.SEPTEMBER, 24);
    endCalendar.set(2018, Calendar.NOVEMBER, 27);

    Strategy strategy =
            new RecurringWeightedInvestmentStrategy(startCalendar.getTime(),
                    stocksWeights,
                    30,
                    endCalendar.getTime());

    try {
      userModel.persistFromModel(new StrategyPersister(null, strategy));
      Assert.fail("should have failed");
    } catch (Exception e) {
      Assert.assertEquals(Constants.INVALID_INPUT, e.getMessage());
    }

    try {
      userModel.persistFromModel(new StrategyPersister(serDes, null));
      Assert.fail("should have failed");
    } catch (Exception e) {
      Assert.assertEquals(Constants.INVALID_INPUT, e.getMessage());
    }

    try {
      userModel.persistFromModel(new StrategyPersister(serDes, strategy));
    } catch (Exception e) {
      Assert.fail(e.getMessage());
    }
  }

  @Test
  public void blankFileLeadsToInvalidJSON() throws IOException {
    PersistableUserModel userModel = TestUtils.getEmptyPersistableUserModel();
    Path test = Utils.getPathInDefaultFolder(Paths.get("blank" + ".json"));

    if (Files.notExists(test)) {
      Files.createFile(test);
    }
    JSONSerDes<Portfolio> portfolioJSONSerDes = new JSONSerDes<>(test,
            new TypeToken<Portfolio>() {
            }.getType());

    JSONSerDes<Strategy> strategyJSONSerder = new JSONSerDes<>(test,
            new TypeToken<Portfolio>() {
            }.getType());

    try {
      userModel.loadIntoModel(new PortfolioLoader(userModel, portfolioJSONSerDes));
      Assert.fail("should have failed");
    } catch (Exception e) {
      Assert.assertEquals("Could not deserialize portfolio", e.getMessage());
    }

    try {
      userModel.loadIntoModel(new StrategyLoader(userModel, strategyJSONSerder, PORTFOLIO_P1,
              new BigDecimal("1000"), 10));
      Assert.fail("should have failed");
    } catch (Exception e) {
      Assert.assertEquals("Could not deserialize strategy", e.getMessage());
    }
  }

  @Test
  public void manuallyCreatedPortfolioWorks() throws IOException {
    PersistableUserModel userModel = TestUtils.getEmptyPersistableUserModel();
    Path manual = Utils.getPathInDefaultFolder(Paths.get("manual_portfolio" + ".json"));

    if (!Files.exists(manual)) {
      throw new FileNotFoundException("add a file that has a manually created portfolio");
    }

    JSONSerDes<Portfolio> jsonSerDes = new JSONSerDes<>(manual, new TypeToken<Portfolio>() {
    }.getType());

    Assert.assertTrue(userModel.getAllPortfolios().isEmpty());

    userModel.loadIntoModel(new PortfolioLoader(userModel, jsonSerDes));

    Assert.assertFalse(userModel.getAllPortfolios().isEmpty());

    Portfolio portfolio = userModel.getAllPortfolios().get(0);

    Assert.assertEquals(2, portfolio.getPurchases().size());
    Assert.assertEquals(new BigDecimal("220000"), portfolio.getValue(Utils.getTodayDate()));
    Assert.assertEquals(new BigDecimal("220100"),
            portfolio.getCostBasisIncludingCommission(Utils.getTodayDate()));
  }


  @Test
  public void manuallyCreatedStrategyWorks() throws IOException {
    PersistableUserModel userModel = TestUtils.getEmptyPersistableUserModel();
    Path manual = Utils.getPathInDefaultFolder(Paths.get("manual_strategy" + ".json"));

    if (!Files.exists(manual)) {
      throw new FileNotFoundException("add a file that has a manually created strategy");
    }

    JSONSerDes<Strategy> jsonSerDes = new JSONSerDes<>(manual,
            new TypeToken<RecurringWeightedInvestmentStrategy>() {
            }.getType());

    Assert.assertTrue(userModel.getAllPortfolios().isEmpty());

    userModel.loadIntoModel(new StrategyLoader(userModel, jsonSerDes, PORTFOLIO_P1,
            new BigDecimal("10000"), 10));

    Assert.assertFalse(userModel.getAllPortfolios().isEmpty());

    Portfolio portfolio = userModel.getPortfolio(PORTFOLIO_P1);

    Assert.assertEquals(22, portfolio.getPurchases().size());
    Assert.assertEquals(new BigDecimal("98934"), portfolio.getValue(Utils.getTodayDate()));
    Assert.assertEquals(TestUtils.getScaledStrippedBigDecimal(new BigDecimal("108827.40"),
            2),
            TestUtils.getScaledStrippedBigDecimal(portfolio.getCostBasisIncludingCommission(
                    Utils.getTodayDate()
            ), 2));
  }
}