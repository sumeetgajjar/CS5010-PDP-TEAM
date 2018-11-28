import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Share;
import util.TestUtils;
import util.Utils;
import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.InsufficientCapitalException;
import virtualgambling.model.exceptions.PortfolioNotFoundException;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.exceptions.StrategyExecutionException;
import virtualgambling.model.stockdao.SimpleStockDAO;
import virtualgambling.model.stockdao.StockDAO;
import virtualgambling.model.stockdatasource.SimpleStockDataSource;
import virtualgambling.model.strategy.OneTimeWeightedInvestmentStrategy;
import virtualgambling.model.strategy.RecurringWeightedInvestmentStrategy;
import virtualgambling.model.strategy.Strategy;

/**
 * The class represents a Junit class to test Enhanced User Model in isolation.
 */
public class EnhancedUserModelTest extends UserModelTest {

  private static final String PORTFOLIO_P1 = "p1";
  private static final String PORTFOLIO_FANG = "FANG";

  @Override
  protected UserModel getUserModel(StockDAO stockDAO) {
    return TestUtils.getEmptyEnhancedUserModelWithStockDAO(stockDAO);
  }

  @Test
  public void totalPercentageOfStockIfNotHundredFails() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 50.0D);
    stocksWeights.put("NFLX", 49.0D);

    Date startDate = TestUtils.getValidDateForTrading();
    Date endDate = TestUtils.getValidDateForTrading();
    try {
      new OneTimeWeightedInvestmentStrategy(startDate, stocksWeights);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Weights do not sum up to 100", e.getMessage());
    }

    try {
      new RecurringWeightedInvestmentStrategy(startDate, stocksWeights, 1, endDate);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Weights do not sum up to 100", e.getMessage());
    }

    try {
      new OneTimeWeightedInvestmentStrategy(startDate, Collections.emptyMap());
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Weights do not sum up to 100", e.getMessage());
    }

    try {
      new RecurringWeightedInvestmentStrategy(startDate, Collections.emptyMap(), 1, endDate);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Weights do not sum up to 100", e.getMessage());
    }

    stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 50.0D);
    stocksWeights.put("NFLX", 51.0D);
    try {
      new OneTimeWeightedInvestmentStrategy(startDate, stocksWeights);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Weights do not sum up to 100", e.getMessage());
    }

    try {
      new RecurringWeightedInvestmentStrategy(startDate, stocksWeights, 1, endDate);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Weights do not sum up to 100", e.getMessage());
    }
  }

  @Test
  public void invalidInputToStrategyFails() {
    try {
      new OneTimeWeightedInvestmentStrategy(TestUtils.getValidDateForTrading(), null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      new OneTimeWeightedInvestmentStrategy(null, Collections.singletonMap("AAPL", 100D));
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    Date startDate = TestUtils.getValidDateForTrading();
    Date endDate = TestUtils.getValidDateForTrading();
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 50.0D);
    stocksWeights.put("NFLX", 50.0D);

    try {
      new RecurringWeightedInvestmentStrategy(null, stocksWeights, 1, endDate);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      new RecurringWeightedInvestmentStrategy(startDate, null, 1, endDate);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    Date futureDate = TestUtils.getFutureTime();
    try {
      new RecurringWeightedInvestmentStrategy(futureDate, stocksWeights, 1, endDate);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Cannot start strategy from a future date", e.getMessage());
    }

    try {
      new RecurringWeightedInvestmentStrategy(futureDate, stocksWeights, 1, futureDate);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Cannot start strategy from a future date", e.getMessage());
    }

    Map<String, Double> aapl = Collections.singletonMap("AAPL", 100D);
    try {
      new OneTimeWeightedInvestmentStrategy(null, aapl);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      new RecurringWeightedInvestmentStrategy(TestUtils.getValidDateForTrading(), aapl, -1);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Frequency cannot be less than 1 day", e.getMessage());
    }

    try {
      new RecurringWeightedInvestmentStrategy(startDate, stocksWeights, 0, futureDate);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Frequency cannot be less than 1 day", e.getMessage());
    }

    try {
      new RecurringWeightedInvestmentStrategy(startDate, stocksWeights, -1, futureDate);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Frequency cannot be less than 1 day", e.getMessage());
    }
  }

  @Test
  public void buySharesFailsForNullInputs() throws StockDataNotFoundException {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    Date date = TestUtils.getValidDateForTrading();
    Share appleShare = getAppleShare();

    try {
      enhancedUserModel.buyShares(null, "p1", date, 1, 10);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      enhancedUserModel.buyShares(appleShare.getTickerName(), null, date, 1, 10);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      enhancedUserModel.buyShares(appleShare.getTickerName(), "p1", null, 1, 10);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      enhancedUserModel.buyShares(null, new BigDecimal(100), getValidStrategy(), 10D);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      enhancedUserModel.buyShares(PORTFOLIO_P1, null, getValidStrategy(), 10D);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }

    try {
      enhancedUserModel.buyShares(PORTFOLIO_P1, new BigDecimal(100), null, 10D);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid input", e.getMessage());
    }
  }

  @Test
  public void buySingleShareWithCommissionOfInvalidQuantityFails() throws IllegalArgumentException,
          StockDataNotFoundException {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    Date date = TestUtils.getValidDateForTrading();
    Share appleShare = getAppleShare();

    enhancedUserModel.createPortfolio("p1");
    try {
      enhancedUserModel.buyShares(appleShare.getTickerName(), "p1", date, 0, 10D);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Quantity has to be positive", e.getMessage());
    }

    try {
      enhancedUserModel.buyShares(appleShare.getTickerName(), "p1", date, -1, 10D);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Quantity has to be positive", e.getMessage());
    }
  }

  @Test
  public void buySingleShareWithCommissionForMissingPortfolioFails() throws StockDataNotFoundException {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    Date date = TestUtils.getValidDateForTrading();
    Share appleShare = getAppleShare();

    try {
      enhancedUserModel.buyShares(appleShare.getTickerName(), "p1", date, 1, 10);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Portfolio not found", e.getMessage());
    }
  }

  @Test
  public void buySingleShareWithCommissionWhoseDataIsNotPresentFails() throws StockDataNotFoundException {
    EnhancedUserModel enhancedUserModel =
            TestUtils.getEmptyEnhancedUserModelWithStockDAO(new SimpleStockDAO(new SimpleStockDataSource()));
    enhancedUserModel.createPortfolio("p1");
    try {
      Calendar calendar = Utils.getCalendarInstance();
      calendar.set(2018, Calendar.JULY, 4, 10, 0);
      calendar.add(Calendar.DATE, -1);
      Date date = Utils.removeTimeFromDate(calendar.getTime());

      enhancedUserModel.buyShares(getAppleShare().getTickerName(), "p1", date, 1, 10);
      Assert.fail("should have failed");
    } catch (StockDataNotFoundException e) {
      Assert.assertEquals(
              "Stock Data not found for Stock:AAPL for Date:Tue Jul 03 00:00:00 EDT 2018",
              e.getMessage());
    }
  }

  @Test
  public void buySingleShareWithCommissionFailsForInvalidTickerName() throws StockDataNotFoundException {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    enhancedUserModel.createPortfolio("p1");
    try {
      enhancedUserModel.buyShares("AAPL1", "p1", TestUtils.getValidDateForTrading(), 1, 10);
      Assert.fail("should have failed");
    } catch (StockDataNotFoundException e) {
      Assert.assertEquals("Stock Data not found", e.getMessage());
    }
  }

  @Test
  public void buySingleShareWithCommissionFailsDueToInsufficientFunds() throws StockDataNotFoundException {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    Date date = TestUtils.getValidDateForTrading();
    enhancedUserModel.createPortfolio("p1");

    try {
      enhancedUserModel.buyShares(getAppleShare().getTickerName(), "p1", date,
              TestUtils.DEFAULT_USER_CAPITAL
                      .divide(BigDecimal.TEN, BigDecimal.ROUND_CEILING).longValue() + 1, 10);
      Assert.fail("should have failed");
    } catch (InsufficientCapitalException e) {
      Assert.assertEquals("Insufficient funds", e.getMessage());
    }
  }

  @Test
  public void buySharesFailsIfInvestmentAmountIsLessThanOne() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("RANDOM", 80.0D);
    stocksWeights.put("NFLX", 20.0D);

    Date validDateForTrading = TestUtils.getValidDateForTrading();
    Strategy strategy = new OneTimeWeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    try {
      enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(0), strategy, 10);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Investment amount cannot be less than 1", e.getMessage());
    }

    try {
      enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(-1), strategy, 10);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Investment amount cannot be less than 1", e.getMessage());
    }
  }

  @Test
  public void buySharesFailsUsingWeightedStrategyWithDifferentWeightsIfOneStockIsInvalid() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("RANDOM", 80.0D);
    stocksWeights.put("NFLX", 20.0D);

    Date validDateForTrading = TestUtils.getValidDateForTrading();
    Strategy strategy = new OneTimeWeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    try {
      enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);
      Assert.fail("should have failed");
    } catch (StockDataNotFoundException e) {
      Assert.assertEquals("Stock Data not found", e.getMessage());
    }
  }

  @Test
  public void buySharesFailsUsingWeightedStrategyWithSameWeightsIfOneStockIsInvalid() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("RANDOM", 50.0D);
    stocksWeights.put("NFLX", 50.0D);

    Date validDateForTrading = TestUtils.getValidDateForTrading();
    Strategy strategy = new OneTimeWeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    try {
      enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);
      Assert.fail("should have failed");
    } catch (StockDataNotFoundException e) {
      Assert.assertEquals("Stock Not found", e.getMessage());
    }
    Assert.assertNull(enhancedUserModel.getPortfolio(PORTFOLIO_FANG));
  }

  @Test
  public void buySharesUsingWeightedStrategyWithDifferentWeightsFailsIfOneStockDataIsNotFound() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 80.0D);
    stocksWeights.put("NFLX", 20.0D);

    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(1990, Calendar.NOVEMBER, 1, 4, 0);
    Date dateOnWhichStockDataIsNotAvailable = calendar.getTime();
    Strategy strategy = new OneTimeWeightedInvestmentStrategy(dateOnWhichStockDataIsNotAvailable,
            stocksWeights);

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    try {
      enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);
      Assert.fail("should have failed");
    } catch (StockDataNotFoundException e) {
      Assert.assertEquals("Stock Not found", e.getMessage());
    }
  }

  @Test
  public void buySharesUsingWeightedStrategyWithSameWeightsFailsIfOneStockDataIsNotFound() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 50.0D);
    stocksWeights.put("NFLX", 50.0D);

    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(1990, Calendar.NOVEMBER, 1, 4, 0);
    Date dateOnWhichStockDataIsNotAvailable = calendar.getTime();
    Strategy strategy = new OneTimeWeightedInvestmentStrategy(dateOnWhichStockDataIsNotAvailable,
            stocksWeights);

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    try {
      enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);
      Assert.fail("should have failed");
    } catch (StockDataNotFoundException e) {
      Assert.assertEquals("Stock Not found", e.getMessage());
    }
  }

  @Test
  public void buySharesUsingWeightedStrategyWithDifferentWeightsInNewPortfolio() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 80.0D);
    stocksWeights.put("NFLX", 20.0D);

    Date validDateForTrading = TestUtils.getValidDateForTrading();
    Strategy strategy = new OneTimeWeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Assert.assertEquals(0, enhancedUserModel.getAllPortfolios().size());

    enhancedUserModel.createPortfolio(PORTFOLIO_FANG);

    List<SharePurchaseOrder> sharePurchaseOrders = enhancedUserModel.buyShares(
            PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);

    Portfolio fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    List<SharePurchaseOrder> purchasesInFANG = fangPortfolio.getPurchases();

    Map<String, Long> shareCount = getIndividualShareCount(purchasesInFANG);
    Assert.assertEquals(Long.valueOf(2), shareCount.get("FB"));
    Assert.assertEquals(Long.valueOf(1), shareCount.get("NFLX"));

    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(110), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getCostBasis(validDateForTrading), 2));
    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(100), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getValue(validDateForTrading), 2));

    BigDecimal actualCostWithCommission = getTotalCostOfPurchase(sharePurchaseOrders);
    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(110), 2),
            getScaledStrippedBigDecimal(actualCostWithCommission, 2));
  }

  @Test
  public void buySharesFailsDueToAmountGivenIsLessThanMinimumRequiredAmount() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 80.0D);
    stocksWeights.put("NFLX", 20.0D);

    Date validDateForTrading = TestUtils.getValidDateForTrading();
    Strategy strategy = new OneTimeWeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    try {
      enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(99), strategy, 10);
      Assert.fail("should have failed");
    } catch (StrategyExecutionException e) {
      Assert.assertEquals("Unable to buy even a single stock",
              e.getMessage());
    }
  }

  @Test
  public void buySharesUsingWeightedStrategyWithThriceTheMinimumAmount() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 80.0D);
    stocksWeights.put("NFLX", 20.0D);

    Date validDateForTrading = TestUtils.getValidDateForTrading();
    Strategy strategy = new OneTimeWeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(300), strategy, 10);

    Portfolio fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    List<SharePurchaseOrder> purchasesInFANG = fangPortfolio.getPurchases();

    Map<String, Long> shareCount = getIndividualShareCount(purchasesInFANG);
    Assert.assertEquals(Long.valueOf(6), shareCount.get("FB"));
    Assert.assertEquals(Long.valueOf(3), shareCount.get("NFLX"));

    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(330), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getCostBasis(validDateForTrading), 2));
    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(300), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getValue(validDateForTrading), 2));
  }

  @Test
  public void buySharesUsingWeightedStrategyWithDifferentWeightsOnMultipleDays() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 50.0D);
    stocksWeights.put("NFLX", 50.0D);

    Calendar calendar = Utils.getCalendarInstance();
    calendar.set(2018, Calendar.NOVEMBER, 1, 10, 0);

    Date day3 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day2 = calendar.getTime();

    calendar.add(Calendar.DATE, -1);
    Date day1 = calendar.getTime();

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    //purchasing the shares on day1
    Strategy strategy = new OneTimeWeightedInvestmentStrategy(day1, stocksWeights);
    enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);

    Portfolio fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    List<SharePurchaseOrder> purchasesInFANG = fangPortfolio.getPurchases();
    Map<String, Long> shareCount = getIndividualShareCount(purchasesInFANG);

    Assert.assertEquals(Long.valueOf(1), shareCount.get("FB"));
    Assert.assertEquals(Long.valueOf(2), shareCount.get("NFLX"));

    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(88), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getCostBasis(day1), 2));
    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(80), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getValue(day1), 2));

    //purchasing the same shares on day2
    strategy = new OneTimeWeightedInvestmentStrategy(day2, stocksWeights);
    enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);

    fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    purchasesInFANG = fangPortfolio.getPurchases();
    shareCount = getIndividualShareCount(purchasesInFANG);

    Assert.assertEquals(Long.valueOf(2), shareCount.get("FB"));
    Assert.assertEquals(Long.valueOf(4), shareCount.get("NFLX"));

    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(176), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getCostBasis(day2), 2));
    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(160), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getValue(day2), 2));

    //purchasing the different shares on day3
    stocksWeights = new HashMap<>();
    stocksWeights.put("T", 50.0D);
    stocksWeights.put("GOOG", 50.0D);
    strategy = new OneTimeWeightedInvestmentStrategy(day3, stocksWeights);
    enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);

    fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    purchasesInFANG = fangPortfolio.getPurchases();
    shareCount = getIndividualShareCount(purchasesInFANG);

    Assert.assertEquals(Long.valueOf(2), shareCount.get("FB"));
    Assert.assertEquals(Long.valueOf(4), shareCount.get("NFLX"));
    Assert.assertEquals(Long.valueOf(5), shareCount.get("T"));
    Assert.assertEquals(Long.valueOf(4), shareCount.get("GOOG"));

    Assert.assertEquals(new BigDecimal(279.4)
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .stripTrailingZeros(),
            getScaledStrippedBigDecimal(fangPortfolio.getCostBasis(day3), 2));
    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(254), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getValue(day3), 2));
  }

  @Test
  public void buySharesUsingWeightedStrategyWithSameWeightsInNewPortfolio() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 50.0D);
    stocksWeights.put("NFLX", 50.0D);

    Date validDateForTrading = TestUtils.getValidDateForTrading();
    Strategy strategy = new OneTimeWeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Assert.assertEquals(0, enhancedUserModel.getAllPortfolios().size());

    List<SharePurchaseOrder> sharePurchaseOrders = enhancedUserModel.buyShares(
            PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);

    Portfolio fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    List<SharePurchaseOrder> purchasesInFANG = fangPortfolio.getPurchases();

    Map<String, Long> shareCount = getIndividualShareCount(purchasesInFANG);
    Assert.assertEquals(Long.valueOf(1), shareCount.get("FB"));
    Assert.assertEquals(Long.valueOf(2), shareCount.get("NFLX"));

    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(88), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getCostBasis(validDateForTrading), 2));
    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(80), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getValue(validDateForTrading), 2));

    BigDecimal actualCostWithCommission = getTotalCostOfPurchase(sharePurchaseOrders);
    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(88), 2),
            getScaledStrippedBigDecimal(actualCostWithCommission, 2));
  }

  @Test
  public void buySharesUsingWeightedStrategyWithDifferentWeightsInOldPortfolio() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    try {
      enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
      Assert.fail("should have failed");
    } catch (PortfolioNotFoundException e) {
      Assert.assertEquals("portfolio by the name 'FANG' not found", e.getMessage());
    }

    enhancedUserModel.createPortfolio(PORTFOLIO_FANG);
    Date validDateForTrading = TestUtils.getValidDateForTrading();
    enhancedUserModel.buyShares("AAPL", PORTFOLIO_FANG, validDateForTrading, 1, 10);

    Portfolio fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    Assert.assertEquals(1, fangPortfolio.getPurchases().size());
    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(11), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getCostBasis(validDateForTrading), 2));
    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(10), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getValue(validDateForTrading), 2));


    Map<String, Long> shareCount = getIndividualShareCount(fangPortfolio.getPurchases());
    Assert.assertEquals(Long.valueOf(1), shareCount.get("AAPL"));

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 80.0D);
    stocksWeights.put("NFLX", 20.0D);
    Strategy strategy = new OneTimeWeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);

    fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    List<SharePurchaseOrder> purchasesInFANG = fangPortfolio.getPurchases();
    Assert.assertEquals(3, purchasesInFANG.size());

    shareCount = getIndividualShareCount(purchasesInFANG);
    Assert.assertEquals(Long.valueOf(2), shareCount.get("FB"));
    Assert.assertEquals(Long.valueOf(1), shareCount.get("AAPL"));
    Assert.assertEquals(Long.valueOf(1), shareCount.get("NFLX"));

    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(121), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getCostBasis(validDateForTrading), 2));
    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(110), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getValue(validDateForTrading), 2));
  }

  @Test
  public void buySharesUsingWeightedStrategyWithSameWeightsInOldPortfolio() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    try {
      enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
      Assert.fail("should have failed");
    } catch (PortfolioNotFoundException e) {
      Assert.assertEquals("portfolio by the name 'FANG' not found", e.getMessage());
    }

    enhancedUserModel.createPortfolio(PORTFOLIO_FANG);

    Date validDateForTrading = TestUtils.getValidDateForTrading();
    enhancedUserModel.buyShares("AAPL", PORTFOLIO_FANG, validDateForTrading, 1, 10);

    Portfolio fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    Assert.assertEquals(1, fangPortfolio.getPurchases().size());

    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(11), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getCostBasis(validDateForTrading), 2));
    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(10), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getValue(validDateForTrading), 2));

    Map<String, Long> shareCount = getIndividualShareCount(fangPortfolio.getPurchases());
    Assert.assertEquals(Long.valueOf(1), shareCount.get("AAPL"));

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 50.0D);
    stocksWeights.put("NFLX", 50.0D);
    Strategy strategy = new OneTimeWeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);

    fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    List<SharePurchaseOrder> purchasesInFANG = fangPortfolio.getPurchases();
    Assert.assertEquals(3, purchasesInFANG.size());

    shareCount = getIndividualShareCount(purchasesInFANG);
    Assert.assertEquals(Long.valueOf(1), shareCount.get("FB"));
    Assert.assertEquals(Long.valueOf(1), shareCount.get("AAPL"));
    Assert.assertEquals(Long.valueOf(2), shareCount.get("NFLX"));

    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(99), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getCostBasis(validDateForTrading), 2));
    Assert.assertEquals(getScaledStrippedBigDecimal(new BigDecimal(90), 2),
            getScaledStrippedBigDecimal(fangPortfolio.getValue(validDateForTrading), 2));
  }

  @Test
  public void buySharesWithNegativeCommissionFails() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    enhancedUserModel.createPortfolio(PORTFOLIO_P1);
    double negativeCommission = -5;
    try {
      enhancedUserModel.buyShares("AAPL", PORTFOLIO_P1, TestUtils.getValidDateForTrading(), 10,
              negativeCommission);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("negative commission rate is not allowed", e.getMessage());
    }


    try {
      enhancedUserModel.buyShares(PORTFOLIO_P1, new BigDecimal(2000), getValidStrategy(),
              negativeCommission);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("negative commission rate is not allowed", e.getMessage());
    }
  }

  @Test
  public void buyingWithCommissionIncreasesCosts() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    enhancedUserModel.createPortfolio(PORTFOLIO_P1);
    double commission = 5;
    SharePurchaseOrder withoutCommission = enhancedUserModel.buyShares("AAPL", PORTFOLIO_P1,
            TestUtils.getValidDateForTrading(), 10);
    SharePurchaseOrder withCommissionWithoutStrategy = enhancedUserModel.buyShares("AAPL",
            PORTFOLIO_P1, TestUtils.getValidDateForTrading(), 10, commission);

    Assert.assertNotEquals(withCommissionWithoutStrategy.getCostOfPurchase(),
            withoutCommission.getCostOfPurchase());

    BigDecimal expectedCostOfPurchaseWithCommission =
            getPriceAfterCommission(withoutCommission.getCostOfPurchase(), commission);

    Assert.assertEquals(getScaledStrippedBigDecimal(expectedCostOfPurchaseWithCommission, 2),
            getScaledStrippedBigDecimal(withCommissionWithoutStrategy.getCostOfPurchase(), 2));
  }

  @Test
  public void buyingWithCommissionAndStrategyIncludesCommissionCosts() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    enhancedUserModel.createPortfolio(PORTFOLIO_P1);
    double commission = 5;

    List<SharePurchaseOrder> sharePurchaseOrders = enhancedUserModel.buyShares(PORTFOLIO_P1,
            new BigDecimal(2000),
            getValidStrategy(), commission);

    for (SharePurchaseOrder sharePurchaseOrder : sharePurchaseOrders) {
      BigDecimal unitPriceIntoQuantity = sharePurchaseOrder.getUnitPrice().multiply(
              BigDecimal.valueOf(sharePurchaseOrder.getQuantity())
      );
      Assert.assertNotEquals(unitPriceIntoQuantity, sharePurchaseOrder.getCostOfPurchase());

      Assert.assertEquals(getScaledStrippedBigDecimal(getPriceAfterCommission(unitPriceIntoQuantity, commission), 2),
              getScaledStrippedBigDecimal(sharePurchaseOrder.getCostOfPurchase(), 2));
    }
  }

  @Test
  public void buyingWithZeroCommissionDoesNotIncreaseCosts() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    enhancedUserModel.createPortfolio(PORTFOLIO_P1);
    double commission = 0.00;
    SharePurchaseOrder withoutCommission = enhancedUserModel.buyShares("AAPL", PORTFOLIO_P1,
            TestUtils.getValidDateForTrading(), 10);
    SharePurchaseOrder withCommissionWithoutStrategy = enhancedUserModel.buyShares("AAPL",
            PORTFOLIO_P1, TestUtils.getValidDateForTrading(), 10, commission);

    Assert.assertEquals(withCommissionWithoutStrategy.getCostOfPurchase(),
            withoutCommission.getCostOfPurchase());

    List<SharePurchaseOrder> sharePurchaseOrders = enhancedUserModel.buyShares(PORTFOLIO_P1,
            new BigDecimal(2000),
            getValidStrategy(), commission);

    for (SharePurchaseOrder sharePurchaseOrder : sharePurchaseOrders) {
      BigDecimal unitPriceIntoQuantity = sharePurchaseOrder.getUnitPrice().multiply(
              BigDecimal.valueOf(sharePurchaseOrder.getQuantity())
      );

      Assert.assertEquals(unitPriceIntoQuantity, sharePurchaseOrder.getCostOfPurchase());
    }

  }

  @Test
  public void buyingWithRecurringStrategyAndSameWeightsWorks() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("AAPL", 50.0D);
    stocksWeights.put("GOOG", 50.0D);

    Calendar startCalendar = Utils.getCalendarInstance();
    Calendar endCalendar = Utils.getCalendarInstance();

    startCalendar.set(2018, Calendar.SEPTEMBER, 24);
    endCalendar.set(2018, Calendar.NOVEMBER, 27);
    Strategy recurringWeightedInvestmentStrategy =
            new RecurringWeightedInvestmentStrategy(startCalendar.getTime(), stocksWeights, 30,
                    endCalendar.getTime());

    enhancedUserModel.createPortfolio(PORTFOLIO_P1);
    for (double commission : Arrays.asList(0, 10)) {
      List<SharePurchaseOrder> sharePurchaseOrders =
              enhancedUserModel.buyShares(PORTFOLIO_P1,
                      new BigDecimal(2000),
                      recurringWeightedInvestmentStrategy, commission);
      Assert.assertEquals(6, sharePurchaseOrders.size());

      for (SharePurchaseOrder sharePurchaseOrder : sharePurchaseOrders) {
        BigDecimal costOfPurchaseWithoutCommission = sharePurchaseOrder.getUnitPrice().multiply(
                BigDecimal.valueOf(sharePurchaseOrder.getQuantity())
        );
        // individual cost cannot be more than 1000 since
        Assert.assertFalse(costOfPurchaseWithoutCommission.compareTo(new BigDecimal(
                1000)) >= 0
        );
      }

      Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
      Long expectedAAPLCount = individualShareCount.get("AAPL");
      Assert.assertEquals(Long.valueOf(333 * 2), expectedAAPLCount);

      Long expectedGOOGCount = individualShareCount.get("GOOG");
      Assert.assertEquals(Long.valueOf(270 * 2), expectedGOOGCount);
    }
  }

  @Test
  public void buyingWithRecurringStrategyAndSameWeightsWithoutEndDateWorks() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("AAPL", 50.0D);
    stocksWeights.put("GOOG", 50.0D);

    Calendar startCalendar = Utils.getCalendarInstance();
    Calendar endCalendar = Utils.getCalendarInstance();

    startCalendar.set(2018, Calendar.SEPTEMBER, 24);
    endCalendar.set(2018, Calendar.NOVEMBER, 27);
    Strategy recurringWeightedInvestmentStrategy =
            new TestUtils.MockRecurringWeightedInvestmentStrategy(startCalendar.getTime(),
                    stocksWeights, 30,
                    endCalendar.getTime());

    enhancedUserModel.createPortfolio(PORTFOLIO_P1);
    for (double commission : Arrays.asList(0, 10)) {
      List<SharePurchaseOrder> sharePurchaseOrders =
              enhancedUserModel.buyShares(PORTFOLIO_P1,
                      new BigDecimal(2000),
                      recurringWeightedInvestmentStrategy, commission);
      Assert.assertEquals(4, sharePurchaseOrders.size());

      for (SharePurchaseOrder sharePurchaseOrder : sharePurchaseOrders) {
        BigDecimal costOfPurchaseWithoutCommission = sharePurchaseOrder.getUnitPrice().multiply(
                BigDecimal.valueOf(sharePurchaseOrder.getQuantity())
        );
        // individual cost cannot be more than 1000 since
        Assert.assertFalse(costOfPurchaseWithoutCommission.compareTo(new BigDecimal(
                1000)) > 0
        );
      }

      Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
      Long expectedAAPLCount = individualShareCount.get("AAPL");
      Assert.assertEquals(Long.valueOf(110), expectedAAPLCount);

      Long expectedGOOGCount = individualShareCount.get("GOOG");
      Assert.assertEquals(Long.valueOf(180), expectedGOOGCount);
    }

    Portfolio portfolio = enhancedUserModel.getPortfolio(PORTFOLIO_P1);
    List<SharePurchaseOrder> sharePurchaseOrders = portfolio.getPurchases();
    Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
    Long expectedAAPLCount = individualShareCount.get("AAPL");
    Assert.assertEquals(Long.valueOf(110 * 2), expectedAAPLCount);

    Long expectedGOOGCount = individualShareCount.get("GOOG");
    Assert.assertEquals(Long.valueOf(180 * 2), expectedGOOGCount);
  }


  @Test
  public void buyingWithRecurringStrategyAndSingleStockWorks() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("GOOG", 100.0D);

    Calendar startCalendar = Utils.getCalendarInstance();
    Calendar endCalendar = Utils.getCalendarInstance();

    startCalendar.set(2018, Calendar.SEPTEMBER, 24);
    endCalendar.set(2018, Calendar.NOVEMBER, 27);
    Strategy recurringWeightedInvestmentStrategy =
            new RecurringWeightedInvestmentStrategy(startCalendar.getTime(), stocksWeights, 30,
                    endCalendar.getTime());

    enhancedUserModel.createPortfolio(PORTFOLIO_P1);
    for (double commission : Arrays.asList(0, 10)) {
      List<SharePurchaseOrder> sharePurchaseOrders = enhancedUserModel.buyShares(PORTFOLIO_P1,
              new BigDecimal(2000),
              recurringWeightedInvestmentStrategy, commission);
      Assert.assertEquals(3, sharePurchaseOrders.size());

      for (SharePurchaseOrder sharePurchaseOrder : sharePurchaseOrders) {
        BigDecimal costOfPurchaseWithoutCommission = sharePurchaseOrder.getUnitPrice().multiply(
                BigDecimal.valueOf(sharePurchaseOrder.getQuantity())
        );
        // individual cost cannot be more than 1000 since
        Assert.assertFalse(costOfPurchaseWithoutCommission.compareTo(new BigDecimal(
                1000)) >= 0
        );
      }

      Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);

      Long expectedGOOGCount = individualShareCount.get("GOOG");
      Assert.assertEquals(Long.valueOf(543), expectedGOOGCount);
    }

    Portfolio portfolio = enhancedUserModel.getPortfolio(PORTFOLIO_P1);
    List<SharePurchaseOrder> sharePurchaseOrders = portfolio.getPurchases();
    Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
    Long expectedGOOGCount = individualShareCount.get("GOOG");
    Assert.assertEquals(Long.valueOf(543 * 2), expectedGOOGCount);
  }

  @Test
  public void buyingWithRecurringStrategyAndDifferentWeightsWorks() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("AAPL", 80.0D);
    stocksWeights.put("GOOG", 20.0D);

    Calendar startCalendar = Utils.getCalendarInstance();
    Calendar endCalendar = Utils.getCalendarInstance();

    startCalendar.set(2018, Calendar.SEPTEMBER, 24);
    endCalendar.set(2018, Calendar.NOVEMBER, 27);
    Strategy recurringWeightedInvestmentStrategy =
            new RecurringWeightedInvestmentStrategy(startCalendar.getTime(), stocksWeights, 30,
                    endCalendar.getTime());

    enhancedUserModel.createPortfolio(PORTFOLIO_P1);
    for (double commission : Arrays.asList(0, 10)) {
      List<SharePurchaseOrder> sharePurchaseOrders = enhancedUserModel.buyShares(PORTFOLIO_P1,
              new BigDecimal(1000),
              recurringWeightedInvestmentStrategy, commission);
      Assert.assertEquals(4, sharePurchaseOrders.size());

      Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
      Long expectedAAPLCount = individualShareCount.get("AAPL");
      Assert.assertEquals(Long.valueOf(88), expectedAAPLCount);

      Long expectedGOOGCount = individualShareCount.get("GOOG");
      Assert.assertEquals(Long.valueOf(36), expectedGOOGCount);
    }

    Portfolio portfolio = enhancedUserModel.getPortfolio(PORTFOLIO_P1);
    List<SharePurchaseOrder> sharePurchaseOrders = portfolio.getPurchases();
    Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
    Long expectedAAPLCount = individualShareCount.get("AAPL");
    Assert.assertEquals(Long.valueOf(88 * 2), expectedAAPLCount);

    Long expectedGOOGCount = individualShareCount.get("GOOG");
    Assert.assertEquals(Long.valueOf(36 * 2), expectedGOOGCount);
  }

  @Test
  public void buyingWithRecurringStrategyAndDifferentWeightsWithoutEndWorks() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("AAPL", 80.0D);
    stocksWeights.put("GOOG", 20.0D);

    Calendar startCalendar = Utils.getCalendarInstance();
    Calendar endCalendar = Utils.getCalendarInstance();

    startCalendar.set(2018, Calendar.SEPTEMBER, 24);
    endCalendar.set(2018, Calendar.NOVEMBER, 27);
    Strategy recurringWeightedInvestmentStrategy =
            new TestUtils.MockRecurringWeightedInvestmentStrategy(startCalendar.getTime(),
                    stocksWeights, 30,
                    endCalendar.getTime());

    enhancedUserModel.createPortfolio(PORTFOLIO_P1);
    for (double commission : Arrays.asList(0, 10)) {
      List<SharePurchaseOrder> sharePurchaseOrders = enhancedUserModel.buyShares(PORTFOLIO_P1,
              new BigDecimal(1000),
              recurringWeightedInvestmentStrategy, commission);
      Assert.assertEquals(4, sharePurchaseOrders.size());

      Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
      Long expectedAAPLCount = individualShareCount.get("AAPL");
      Assert.assertEquals(Long.valueOf(88), expectedAAPLCount);

      Long expectedGOOGCount = individualShareCount.get("GOOG");
      Assert.assertEquals(Long.valueOf(36), expectedGOOGCount);
    }

    Portfolio portfolio = enhancedUserModel.getPortfolio(PORTFOLIO_P1);
    List<SharePurchaseOrder> sharePurchaseOrders = portfolio.getPurchases();
    Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
    Long expectedAAPLCount = individualShareCount.get("AAPL");
    Assert.assertEquals(Long.valueOf(88 * 2), expectedAAPLCount);

    Long expectedGOOGCount = individualShareCount.get("GOOG");
    Assert.assertEquals(Long.valueOf(36 * 2), expectedGOOGCount);
  }

  @Test
  public void buyingWithRecurringStrategyAndVariableFrequency() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 80.0D);
    stocksWeights.put("GOOG", 20.0D);

    Calendar startCalendar = Utils.getCalendarInstance();
    Calendar endCalendar = Utils.getCalendarInstance();

    startCalendar.set(2018, Calendar.SEPTEMBER, 24);
    endCalendar.set(2018, Calendar.NOVEMBER, 27);

    long numberOfDaysBetweenStartDateAndEndDate = Utils.absoluteDaysBetweenDates(
            startCalendar.getTime(), endCalendar.getTime()
    );

    long totalNumberOfPurchases = 0;
    int amountToInvest = 1000;

    for (int dayFrequency : Arrays.asList(5, 10, 30, 50)) {
      long numberOfPurchases = numberOfDaysBetweenStartDateAndEndDate / dayFrequency;
      totalNumberOfPurchases += numberOfPurchases;

      Strategy recurringWeightedInvestmentStrategy =
              new RecurringWeightedInvestmentStrategy(startCalendar.getTime(), stocksWeights,
                      dayFrequency,
                      endCalendar.getTime());

      enhancedUserModel.createPortfolio(PORTFOLIO_P1);
      for (double commission : Arrays.asList(0, 10)) {
        List<SharePurchaseOrder> sharePurchaseOrders = enhancedUserModel.buyShares(PORTFOLIO_P1,
                new BigDecimal(amountToInvest),
                recurringWeightedInvestmentStrategy, commission);

        Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
        double numberOfFBStocksIn1Transaction =
                (stocksWeights.get("FB") * amountToInvest) / (100 * 40);
        double numberOfGoogStocksIn1Transaction =
                (stocksWeights.get("GOOG") * amountToInvest) / (100 * 11);
        Assert.assertEquals(numberOfFBStocksIn1Transaction * numberOfPurchases,
                individualShareCount.get("FB"), 0.0);
        Assert.assertEquals(numberOfGoogStocksIn1Transaction * numberOfPurchases,
                individualShareCount.get("GOOG"), 0.0);
      }
    }

    Portfolio portfolio = enhancedUserModel.getPortfolio(PORTFOLIO_P1);
    List<SharePurchaseOrder> sharePurchaseOrders = portfolio.getPurchases();
    Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
    double numberOfFBStocksIn1Transaction = (stocksWeights.get("FB") * amountToInvest) / 40;
    double numberOfGoogStocksIn1Transaction = (stocksWeights.get("GOOG") * amountToInvest) / 11;
    Assert.assertEquals(numberOfFBStocksIn1Transaction * totalNumberOfPurchases,
            individualShareCount.get("FB"), 0.0);
    Assert.assertEquals(numberOfGoogStocksIn1Transaction * totalNumberOfPurchases,
            individualShareCount.get("GOOG"), 0.0);
  }

  @Test
  public void allocatingMultipleStrategiesToSamePortfolioWorks() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Map<String, Double> stocksWeights1 = new HashMap<>();
    stocksWeights1.put("AAPL", 80.0D);
    stocksWeights1.put("GOOG", 20.0D);


    Map<String, Double> stocksWeights2 = new HashMap<>();
    stocksWeights2.put("T", 80.0D);
    stocksWeights2.put("NFLX", 20.0D);

    Calendar startCalendar1 = Utils.getCalendarInstance();
    Calendar startCalendar2 = Utils.getCalendarInstance();

    Calendar endCalendar1 = Utils.getCalendarInstance();
    Calendar endCalendar2 = Utils.getCalendarInstance();

    startCalendar1.set(2018, Calendar.SEPTEMBER, 24);
    endCalendar1.set(2018, Calendar.NOVEMBER, 27);

    startCalendar2.set(2018, Calendar.JANUARY, 1);
    endCalendar1.set(2018, Calendar.NOVEMBER, 27);


    Strategy recurringWeightedInvestmentStrategy1 =
            new RecurringWeightedInvestmentStrategy(startCalendar1.getTime(),
                    stocksWeights1, 30,
                    endCalendar1.getTime());

    Strategy recurringWeightedInvestmentStrategy2 =
            new TestUtils.MockRecurringWeightedInvestmentStrategy(startCalendar2.getTime(),
                    stocksWeights2, 15,
                    endCalendar2.getTime());

//    enhancedUserModel.createPortfolio(PORTFOLIO_P1); you don't need to create a portfolio, it
//    will be auto created if required.

    int amountToInvest = 1000;
    for (double commission : Arrays.asList(0, 10)) {
      List<SharePurchaseOrder> sharePurchaseOrders1 = enhancedUserModel.buyShares(PORTFOLIO_P1,
              new BigDecimal(amountToInvest),
              recurringWeightedInvestmentStrategy1, commission);

      List<SharePurchaseOrder> sharePurchaseOrders2 = enhancedUserModel.buyShares(PORTFOLIO_P1,
              new BigDecimal(amountToInvest),
              recurringWeightedInvestmentStrategy2, commission);

      Assert.assertEquals(6, sharePurchaseOrders1.size());
      Assert.assertEquals(22, sharePurchaseOrders2.size());

      Map<String, Long> individualShareCount1 = getIndividualShareCount(sharePurchaseOrders1);
      Long expectedAAPLCount = individualShareCount1.get("AAPL");
      Assert.assertEquals(Long.valueOf(264), expectedAAPLCount);

      Long expectedGOOGCount = individualShareCount1.get("GOOG");
      Assert.assertEquals(Long.valueOf(54), expectedGOOGCount);


      Map<String, Long> individualShareCount2 = getIndividualShareCount(sharePurchaseOrders1);
      Long expectedTCount = individualShareCount2.get("T");
      Assert.assertEquals(Long.valueOf(1760), expectedTCount);

      Long expectedNFLXCount = individualShareCount2.get("NFLX");
      Assert.assertEquals(Long.valueOf(220), expectedNFLXCount);
    }

    Portfolio portfolio = enhancedUserModel.getPortfolio(PORTFOLIO_P1);
    List<SharePurchaseOrder> sharePurchaseOrders = portfolio.getPurchases();
    Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
    Long expectedAAPLCount = individualShareCount.get("AAPL");
    Assert.assertEquals(Long.valueOf(264 * 2), expectedAAPLCount);

    Long expectedGOOGCount = individualShareCount.get("GOOG");
    Assert.assertEquals(Long.valueOf(54 * 2), expectedGOOGCount);

    Long expectedTCount = individualShareCount.get("T");
    Assert.assertEquals(Long.valueOf(1760 * 2), expectedTCount);

    Long expectedNFLXCount = individualShareCount.get("NFLX");
    Assert.assertEquals(Long.valueOf(220 * 2), expectedNFLXCount);
  }

  @Test
  public void buyingInExistingPortfolioWithRecurringStrategy() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    enhancedUserModel.createPortfolio(PORTFOLIO_P1);
    enhancedUserModel.buyShares("T",
            PORTFOLIO_P1,
            TestUtils.getValidDateForTrading(),
            10);

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("AAPL", 80.0D);
    stocksWeights.put("GOOG", 20.0D);

    Calendar startCalendar = Utils.getCalendarInstance();
    Calendar endCalendar = Utils.getCalendarInstance();

    startCalendar.set(2018, Calendar.SEPTEMBER, 24);
    endCalendar.set(2018, Calendar.NOVEMBER, 27);
    Strategy recurringWeightedInvestmentStrategy =
            new TestUtils.MockRecurringWeightedInvestmentStrategy(startCalendar.getTime(),
                    stocksWeights, 30,
                    endCalendar.getTime());

    for (double commission : Arrays.asList(0, 10)) {
      List<SharePurchaseOrder> sharePurchaseOrders = enhancedUserModel.buyShares(PORTFOLIO_P1,
              new BigDecimal(1000),
              recurringWeightedInvestmentStrategy, commission);
      Assert.assertEquals(4, sharePurchaseOrders.size());

      Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
      Long expectedAAPLCount = individualShareCount.get("AAPL");
      Assert.assertEquals(Long.valueOf(88), expectedAAPLCount);

      Long expectedGOOGCount = individualShareCount.get("GOOG");
      Assert.assertEquals(Long.valueOf(36), expectedGOOGCount);
    }

    Portfolio portfolio = enhancedUserModel.getPortfolio(PORTFOLIO_P1);
    List<SharePurchaseOrder> purchases = portfolio.getPurchases();

    Map<String, Long> individualShareCount = getIndividualShareCount(purchases);
    Assert.assertEquals(9, purchases.size());

    Long expectedAAPLCount = individualShareCount.get("AAPL");
    Assert.assertEquals(Long.valueOf(176), expectedAAPLCount);

    Long expectedGOOGCount = individualShareCount.get("GOOG");
    Assert.assertEquals(Long.valueOf(72), expectedGOOGCount);

    Long expectedTCount = individualShareCount.get("T");
    Assert.assertEquals(Long.valueOf(10), expectedTCount);
  }

  @Test
  public void buyingWithIntervalGreaterThanEndDate() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("AAPL", 80.0D);
    stocksWeights.put("GOOG", 20.0D);

    Calendar startCalendar = Utils.getCalendarInstance();
    Calendar endCalendar = Utils.getCalendarInstance();

    startCalendar.set(2018, Calendar.SEPTEMBER, 24);
    endCalendar.set(2018, Calendar.NOVEMBER, 27);

    RecurringWeightedInvestmentStrategy recurringWeightedInvestmentStrategy =
            new TestUtils.MockRecurringWeightedInvestmentStrategy(startCalendar.getTime(),
                    stocksWeights, 300,
                    endCalendar.getTime());

    enhancedUserModel.createPortfolio(PORTFOLIO_P1);
    for (double commission : Arrays.asList(0, 10)) {
      List<SharePurchaseOrder> sharePurchaseOrders = enhancedUserModel.buyShares(PORTFOLIO_P1,
              new BigDecimal(1000),
              recurringWeightedInvestmentStrategy, commission);
      Assert.assertEquals(2, sharePurchaseOrders.size());

      Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
      Long expectedAAPLCount = individualShareCount.get("AAPL");
      Assert.assertEquals(Long.valueOf(80), expectedAAPLCount);

      Long expectedGOOGCount = individualShareCount.get("GOOG");
      Assert.assertEquals(Long.valueOf(18), expectedGOOGCount);
    }

    Portfolio portfolio = enhancedUserModel.getPortfolio(PORTFOLIO_P1);
    List<SharePurchaseOrder> purchases = portfolio.getPurchases();

    Map<String, Long> individualShareCount = getIndividualShareCount(purchases);
    Assert.assertEquals(4, purchases.size());

    Long expectedAAPLCount = individualShareCount.get("AAPL");
    Assert.assertEquals(Long.valueOf(80 * 2), expectedAAPLCount);

    Long expectedGOOGCount = individualShareCount.get("GOOG");
    Assert.assertEquals(Long.valueOf(18 * 2), expectedGOOGCount);
  }

  @Test
  public void strategyShouldBeAbleToBuyAtleast1StockForEachTicker() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("AAPL", 80.0D);
    stocksWeights.put("GOOG", 20.0D);

    Calendar startCalendar = Utils.getCalendarInstance();
    Calendar endCalendar = Utils.getCalendarInstance();

    startCalendar.set(2018, Calendar.SEPTEMBER, 24);
    endCalendar.set(2018, Calendar.NOVEMBER, 27);
    Strategy recurringWeightedInvestmentStrategy =
            new TestUtils.MockRecurringWeightedInvestmentStrategy(startCalendar.getTime(),
                    stocksWeights, 30,
                    endCalendar.getTime());

    for (double commission : Arrays.asList(0, 10)) {
      try {
        enhancedUserModel.buyShares(PORTFOLIO_P1, new BigDecimal(10),
                recurringWeightedInvestmentStrategy, commission);
        Assert.fail("should have failed");
      } catch (StrategyExecutionException e) {
        Assert.assertEquals("Unable to buy even a single stock", e.getMessage());
      }
    }
  }

  @Test
  public void endDateOfShouldNotBeBeforeTheStartDateForRecurringStrategy() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("AAPL", 80.0D);
    stocksWeights.put("GOOG", 20.0D);

    Calendar startCalendar = Utils.getCalendarInstance();
    Calendar endCalendar = Utils.getCalendarInstance();

    startCalendar.set(2018, Calendar.SEPTEMBER, 24);
    endCalendar.set(2018, Calendar.AUGUST, 27);
    try {
      new RecurringWeightedInvestmentStrategy(startCalendar.getTime(),
              stocksWeights, 30,
              endCalendar.getTime());
      Assert.fail("Should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("end date cannot be before the start date", e.getMessage());
    }
  }

  @Test
  public void startDateCannotBeToday() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("AAPL", 80.0D);
    stocksWeights.put("GOOG", 20.0D);

    try {
      new OneTimeWeightedInvestmentStrategy(Utils.getCalendarInstance().getTime(), stocksWeights);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Strategy cannot start from today", e.getMessage());
    }

    try {
      new RecurringWeightedInvestmentStrategy(Utils.getTodayDate(),
              stocksWeights, 30);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Strategy cannot start from today", e.getMessage());
    }

    try {
      new RecurringWeightedInvestmentStrategy(Utils.getTodayDate(),
              stocksWeights, 30, Utils.getTodayDate());
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Strategy cannot start from today", e.getMessage());
    }
  }

  @Test
  public void strategyCanEndInTheFutureAndBehavesTheSameAsThereIsNoEndDate() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("AAPL", 50.0D);
    stocksWeights.put("GOOG", 50.0D);

    Calendar startCalendar = Utils.getCalendarInstance();
    Calendar endCalendar = Utils.getCalendarInstance();

    startCalendar.set(2018, Calendar.SEPTEMBER, 24);
    endCalendar.set(2218, Calendar.NOVEMBER, 27);
    Strategy recurringWeightedInvestmentStrategy =
            new RecurringWeightedInvestmentStrategy(startCalendar.getTime(),
                    stocksWeights, 30,
                    endCalendar.getTime());

    enhancedUserModel.createPortfolio(PORTFOLIO_P1);
    for (double commission : Arrays.asList(0, 10)) {
      List<SharePurchaseOrder> sharePurchaseOrders =
              enhancedUserModel.buyShares(PORTFOLIO_P1,
                      new BigDecimal(2000),
                      recurringWeightedInvestmentStrategy, commission);
      Assert.assertEquals(4, sharePurchaseOrders.size());

      for (SharePurchaseOrder sharePurchaseOrder : sharePurchaseOrders) {
        BigDecimal costOfPurchaseWithoutCommission = sharePurchaseOrder.getUnitPrice().multiply(
                BigDecimal.valueOf(sharePurchaseOrder.getQuantity())
        );
        // individual cost cannot be more than 1000 since
        Assert.assertFalse(costOfPurchaseWithoutCommission.compareTo(new BigDecimal(
                1000)) > 0
        );
      }

      Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
      Long expectedAAPLCount = individualShareCount.get("AAPL");
      Assert.assertEquals(Long.valueOf(110), expectedAAPLCount);

      Long expectedGOOGCount = individualShareCount.get("GOOG");
      Assert.assertEquals(Long.valueOf(180), expectedGOOGCount);
    }

    Portfolio portfolio = enhancedUserModel.getPortfolio(PORTFOLIO_P1);
    List<SharePurchaseOrder> sharePurchaseOrders = portfolio.getPurchases();
    Map<String, Long> individualShareCount = getIndividualShareCount(sharePurchaseOrders);
    Long expectedAAPLCount = individualShareCount.get("AAPL");
    Assert.assertEquals(Long.valueOf(110 * 2), expectedAAPLCount);

    Long expectedGOOGCount = individualShareCount.get("GOOG");
    Assert.assertEquals(Long.valueOf(180 * 2), expectedGOOGCount);
  }

  private static BigDecimal getPriceAfterCommission(BigDecimal price, double commission) {
    return price.multiply(BigDecimal.valueOf(1 + commission / 100));
  }

  private Strategy getValidStrategy() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 50.0D);
    stocksWeights.put("NFLX", 50.0D);
    return new OneTimeWeightedInvestmentStrategy(TestUtils.getValidDateForTrading(), stocksWeights);
  }

  private Map<String, Long> getIndividualShareCount(List<SharePurchaseOrder> sharePurchaseOrders) {
    Map<String, Long> shareCount = new HashMap<>();
    for (SharePurchaseOrder sharePurchaseOrder : sharePurchaseOrders) {
      Long count = shareCount.getOrDefault(sharePurchaseOrder.getTickerName(), 0L);
      count += sharePurchaseOrder.getQuantity();
      shareCount.put(sharePurchaseOrder.getTickerName(), count);
    }
    return shareCount;
  }

  private BigDecimal getTotalCostOfPurchase(List<SharePurchaseOrder> sharePurchaseOrders) {
    BigDecimal actualCost = BigDecimal.ZERO;
    for (SharePurchaseOrder order : sharePurchaseOrders) {
      actualCost = actualCost.add(order.getCostOfPurchase());
    }
    return actualCost;
  }

  private BigDecimal getScaledStrippedBigDecimal(BigDecimal bigDecimal, int scale) {
    return bigDecimal.setScale(2, RoundingMode.HALF_DOWN).stripTrailingZeros();
  }
}
