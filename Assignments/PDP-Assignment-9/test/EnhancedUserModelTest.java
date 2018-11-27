import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Share;
import util.TestUtils;
import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.stockdao.DAOV2;
import virtualgambling.model.stockdao.StockDAO;
import virtualgambling.model.stockdatasource.AlphaVantageAPIStockDataSource;
import virtualgambling.model.strategy.RecurringWeightedInvestmentStrategy;
import virtualgambling.model.strategy.Strategy;
import virtualgambling.model.strategy.WeightedInvestmentStrategy;

public class EnhancedUserModelTest extends UserModelTest {

  private static final String PORTFOLIO_P1 = "p1";
  private static final String PORTFOLIO_FANG = "FANG";

  @Override
  protected UserModel getUserModel(StockDAO stockDAO) {
    return TestUtils.getEmptyEnhancedUserModelWithStockDAO(stockDAO);
  }

  private EnhancedUserModel getEnhancedUserModel(StockDAO stockDAO) {
    return TestUtils.getEmptyEnhancedUserModelWithStockDAO(stockDAO);
  }

  private StockDAO getLiveStockDAO() {
    return new DAOV2(AlphaVantageAPIStockDataSource.getInstance());
  }

  @Test
  public void totalPercentageOfStockIfNotHundredFails() {
    try {
      Map<String, Double> stocksWeights = new HashMap<>();
      stocksWeights.put("FB", 50.0D);
      stocksWeights.put("NFLX", 49.0D);
      new WeightedInvestmentStrategy(getValidDateForTrading(), stocksWeights);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Weights do not sum up to 100", e.getMessage());
    }

    try {
      new WeightedInvestmentStrategy(getValidDateForTrading(), Collections.emptyMap());
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid Input", e.getMessage());
    }

    try {
      Map<String, Double> stocksWeights = new HashMap<>();
      stocksWeights.put("FB", 50.0D);
      stocksWeights.put("NFLX", 51.0D);
      new WeightedInvestmentStrategy(getValidDateForTrading(), stocksWeights);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid Input", e.getMessage());
    }

    try {
      Map<String, Double> stocksWeights = new HashMap<>();
      stocksWeights.put("FB", 50.0D);
      stocksWeights.put("NFLX", 49.0D);
      new RecurringWeightedInvestmentStrategy(
              new WeightedInvestmentStrategy(
                      getValidDateForTrading(), stocksWeights));
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Weights do not sum up to 100", e.getMessage());
    }

    try {
      new RecurringWeightedInvestmentStrategy(
              new WeightedInvestmentStrategy(
                      getValidDateForTrading(), Collections.emptyMap()));
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid Input", e.getMessage());
    }

    try {
      Map<String, Double> stocksWeights = new HashMap<>();
      stocksWeights.put("FB", 50.0D);
      stocksWeights.put("NFLX", 51.0D);
      new RecurringWeightedInvestmentStrategy(
              new WeightedInvestmentStrategy(
                      getValidDateForTrading(), stocksWeights));
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid Input", e.getMessage());
    }
  }

  @Test
  public void invalidInputToStrategyFails() {
    try {
      new WeightedInvestmentStrategy(getValidDateForTrading(), null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid Input", e.getMessage());
    }

    try {
      new WeightedInvestmentStrategy(null, Collections.singletonMap("AAPL", 100D));
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid Input", e.getMessage());
    }

    try {
      new RecurringWeightedInvestmentStrategy(null);
      Assert.fail("should have failed");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Invalid Input", e.getMessage());
    }
  }

  @Test
  public void buyingShareFailsForNullInputs() throws StockDataNotFoundException {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    Date date = getValidDateForTrading();
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
  public void buySharesIfInvestmentAmountIsLessThanOne() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("RANDOM", 80.0D);
    stocksWeights.put("NFLX", 20.0D);

    Date validDateForTrading = getValidDateForTrading();
    Strategy strategy = new WeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    Assert.assertNull(enhancedUserModel.getPortfolio(PORTFOLIO_FANG));
    try {
      enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(0), strategy, 10);
      Assert.fail("should have failed");
    } catch (StockDataNotFoundException e) {
      Assert.assertEquals("Investment amount cannot be less than 1", e.getMessage());
    }
    Assert.assertNull(enhancedUserModel.getPortfolio(PORTFOLIO_FANG));

    try {
      enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(-1), strategy, 10);
      Assert.fail("should have failed");
    } catch (StockDataNotFoundException e) {
      Assert.assertEquals("Investment amount cannot be less than 1", e.getMessage());
    }
    Assert.assertNull(enhancedUserModel.getPortfolio(PORTFOLIO_FANG));
  }

  @Test
  public void buySharesFailsIfOneStockIsInvalid() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("RANDOM", 80.0D);
    stocksWeights.put("NFLX", 20.0D);

    Date validDateForTrading = getValidDateForTrading();
    Strategy strategy = new WeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    Assert.assertNull(enhancedUserModel.getPortfolio(PORTFOLIO_FANG));
    try {
      enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);
      Assert.fail("should have failed");
    } catch (StockDataNotFoundException e) {
      Assert.assertEquals("Stock Not found", e.getMessage());
    }
    Assert.assertNull(enhancedUserModel.getPortfolio(PORTFOLIO_FANG));
  }

  @Test
  public void buySharesFailsIfOneStockDataIsNotFound() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 80.0D);
    stocksWeights.put("NFLX", 20.0D);

    Calendar calendar = Calendar.getInstance();
    calendar.set(1990, Calendar.NOVEMBER, 1, 4, 0);
    Date dateOnWhichStockDataIsNotAvailable = calendar.getTime();
    Strategy strategy = new WeightedInvestmentStrategy(dateOnWhichStockDataIsNotAvailable,
            stocksWeights);

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    Assert.assertNull(enhancedUserModel.getPortfolio(PORTFOLIO_FANG));
    try {
      enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);
      Assert.fail("should have failed");
    } catch (StockDataNotFoundException e) {
      Assert.assertEquals("Stock Not found", e.getMessage());
    }
    Assert.assertNull(enhancedUserModel.getPortfolio(PORTFOLIO_FANG));
  }

  @Test
  public void buySharesWithDifferentWeightsInNewPortfolio() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 80.0D);
    stocksWeights.put("NFLX", 20.0D);

    Date validDateForTrading = getValidDateForTrading();
    Strategy strategy = new WeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Assert.assertNull(enhancedUserModel.getPortfolio(PORTFOLIO_FANG));
    Assert.assertEquals(0, enhancedUserModel.getAllPortfolios().size());

    List<SharePurchaseOrder> sharePurchaseOrders = enhancedUserModel.buyShares(
            PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);

    Portfolio fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    List<SharePurchaseOrder> purchasesInFANG = fangPortfolio.getPurchases();

    Map<String, Long> shareCount = getIndividualShareCount(purchasesInFANG);
    Assert.assertEquals(Long.valueOf(2), shareCount.get("FB"));
    Assert.assertEquals(Long.valueOf(1), shareCount.get("NFLX"));

    Assert.assertEquals(new BigDecimal(110), fangPortfolio.getCostBasis(validDateForTrading));
    Assert.assertEquals(new BigDecimal(100), fangPortfolio.getValue(validDateForTrading));

    BigDecimal actualCostWithCommission = getTotalCostOfPurchase(sharePurchaseOrders);
    Assert.assertEquals(new BigDecimal(110), actualCostWithCommission);
  }

  @Test
  public void buySharesWithSameWeightsInNewPortfolio() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 50.0D);
    stocksWeights.put("NFLX", 50.0D);

    Date validDateForTrading = getValidDateForTrading();
    Strategy strategy = new WeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Assert.assertNull(enhancedUserModel.getPortfolio(PORTFOLIO_FANG));
    Assert.assertEquals(0, enhancedUserModel.getAllPortfolios().size());

    List<SharePurchaseOrder> sharePurchaseOrders = enhancedUserModel.buyShares(
            PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);

    Portfolio fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    List<SharePurchaseOrder> purchasesInFANG = fangPortfolio.getPurchases();

    Map<String, Long> shareCount = getIndividualShareCount(purchasesInFANG);
    Assert.assertEquals(Long.valueOf(1), shareCount.get("FB"));
    Assert.assertEquals(Long.valueOf(4), shareCount.get("NFLX"));

    Assert.assertEquals(new BigDecimal(88), fangPortfolio.getCostBasis(validDateForTrading));
    Assert.assertEquals(new BigDecimal(80), fangPortfolio.getValue(validDateForTrading));

    BigDecimal actualCostWithCommission = getTotalCostOfPurchase(sharePurchaseOrders);
    Assert.assertEquals(new BigDecimal(88), actualCostWithCommission);
  }

  @Test
  public void buySharesWithDifferentWeightsInOldPortfolio() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Assert.assertNull(enhancedUserModel.getPortfolio(PORTFOLIO_FANG));

    Date validDateForTrading = getValidDateForTrading();
    enhancedUserModel.buyShares("AAPL", PORTFOLIO_FANG, validDateForTrading, 1, 10);

    Portfolio fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    Assert.assertEquals(1, fangPortfolio.getPurchases().size());
    Assert.assertEquals(new BigDecimal(11), fangPortfolio.getCostBasis(validDateForTrading));
    Assert.assertEquals(new BigDecimal(10), fangPortfolio.getValue(validDateForTrading));

    Map<String, Long> shareCount = getIndividualShareCount(fangPortfolio.getPurchases());
    Assert.assertEquals(Long.valueOf(1), shareCount.get("AAPL"));

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 80.0D);
    stocksWeights.put("NFLX", 20.0D);
    Strategy strategy = new WeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);

    fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    List<SharePurchaseOrder> purchasesInFANG = fangPortfolio.getPurchases();
    Assert.assertEquals(3, purchasesInFANG.size());

    shareCount = getIndividualShareCount(purchasesInFANG);
    Assert.assertEquals(Long.valueOf(2), shareCount.get("FB"));
    Assert.assertEquals(Long.valueOf(1), shareCount.get("AAPL"));
    Assert.assertEquals(Long.valueOf(1), shareCount.get("NFLX"));

    Assert.assertEquals(new BigDecimal(121), fangPortfolio.getCostBasis(validDateForTrading));
    Assert.assertEquals(new BigDecimal(110), fangPortfolio.getValue(validDateForTrading));
  }

  @Test
  public void buySharesWithSameWeightsInOldPortfolio() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();

    Assert.assertNull(enhancedUserModel.getPortfolio(PORTFOLIO_FANG));

    Date validDateForTrading = getValidDateForTrading();
    enhancedUserModel.buyShares("AAPL", PORTFOLIO_FANG, validDateForTrading, 1, 10);

    Portfolio fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    Assert.assertEquals(1, fangPortfolio.getPurchases().size());
    Assert.assertEquals(new BigDecimal(11), fangPortfolio.getCostBasis(validDateForTrading));
    Assert.assertEquals(new BigDecimal(10), fangPortfolio.getValue(validDateForTrading));

    Map<String, Long> shareCount = getIndividualShareCount(fangPortfolio.getPurchases());
    Assert.assertEquals(Long.valueOf(1), shareCount.get("AAPL"));

    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 50.0D);
    stocksWeights.put("NFLX", 50.0D);
    Strategy strategy = new WeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    enhancedUserModel.buyShares(PORTFOLIO_FANG, new BigDecimal(100), strategy, 10);

    fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    List<SharePurchaseOrder> purchasesInFANG = fangPortfolio.getPurchases();
    Assert.assertEquals(3, purchasesInFANG.size());

    shareCount = getIndividualShareCount(purchasesInFANG);
    Assert.assertEquals(Long.valueOf(2), shareCount.get("FB"));
    Assert.assertEquals(Long.valueOf(1), shareCount.get("AAPL"));
    Assert.assertEquals(Long.valueOf(1), shareCount.get("NFLX"));

    Assert.assertEquals(new BigDecimal(99), fangPortfolio.getCostBasis(validDateForTrading));
    Assert.assertEquals(new BigDecimal(90), fangPortfolio.getValue(validDateForTrading));
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

  @Test
  public void buySharesWithNegativeCommissionFails() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    enhancedUserModel.createPortfolio(PORTFOLIO_P1);
    double negativeCommission = -5;
    try {
      enhancedUserModel.buyShares("AAPL", PORTFOLIO_P1, getValidDateForTrading(), 10,
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
            getValidDateForTrading(), 10);
    SharePurchaseOrder withCommissionWithoutStrategy = enhancedUserModel.buyShares("AAPL",
            PORTFOLIO_P1, getValidDateForTrading(), 10, commission);

    Assert.assertNotEquals(withCommissionWithoutStrategy.getCostOfPurchase(),
            withoutCommission.getCostOfPurchase());

    Assert.assertEquals(getPriceAfterCommission(withoutCommission.getCostOfPurchase(), commission),
            withCommissionWithoutStrategy.getCostOfPurchase());
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

      Assert.assertEquals(getPriceAfterCommission(unitPriceIntoQuantity, commission),
              sharePurchaseOrder.getCostOfPurchase()
      );
    }
  }

  @Test
  public void buyingWithZeroCommissionDoesNotIncreaseCosts() {
    EnhancedUserModel enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    enhancedUserModel.createPortfolio(PORTFOLIO_P1);
    double commission = 0.00;
    SharePurchaseOrder withoutCommission = enhancedUserModel.buyShares("AAPL", PORTFOLIO_P1,
            getValidDateForTrading(), 10);
    SharePurchaseOrder withCommissionWithoutStrategy = enhancedUserModel.buyShares("AAPL",
            PORTFOLIO_P1, getValidDateForTrading(), 10, commission);

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

  private static BigDecimal getPriceAfterCommission(BigDecimal price, double commission) {
    return price.multiply(BigDecimal.valueOf(1 + commission));
  }

  private Strategy getValidStrategy() {
    //todo
    return null;
  }
}
