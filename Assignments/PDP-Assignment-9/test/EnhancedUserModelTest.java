import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.TestUtils;
import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.stockdao.DAOV2;
import virtualgambling.model.stockdao.StockDAO;
import virtualgambling.model.stockdatasource.AlphaVantageAPIStockDataSource;
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
  public void buyStocksWithDifferentWeightsInNewPortfolio() {
    Map<String, Double> stocksWeights = new HashMap<>();
    stocksWeights.put("FB", 40.0D);
    stocksWeights.put("AAPL", 30.0D);
    stocksWeights.put("NFLX", 20.0D);
    stocksWeights.put("GOOG", 10.0D);

    Date validDateForTrading = getValidDateForTrading();
    Strategy strategy = new WeightedInvestmentStrategy(validDateForTrading, stocksWeights);

    EnhancedUserModel enhancedUserModel = this.getEnhancedUserModel(getLiveStockDAO());

    Assert.assertNull(enhancedUserModel.getPortfolio(PORTFOLIO_FANG));
    Assert.assertEquals(0, enhancedUserModel.getAllPortfolios().size());

    List<SharePurchaseOrder> sharePurchaseOrders = enhancedUserModel.buyShares(
            PORTFOLIO_FANG, new BigDecimal(112), strategy, 10);

    Portfolio fangPortfolio = enhancedUserModel.getPortfolio(PORTFOLIO_FANG);
    List<SharePurchaseOrder> purchasesInFANG = fangPortfolio.getPurchases();

    Map<String, Long> shareCount = getIndividualShareCount(purchasesInFANG);
    Assert.assertEquals(Long.valueOf(1), shareCount.get("FB"));
    Assert.assertEquals(Long.valueOf(3), shareCount.get("AAPL"));
    Assert.assertEquals(Long.valueOf(1), shareCount.get("NFLX"));
    Assert.assertEquals(Long.valueOf(1), shareCount.get("GOOG"));

    BigDecimal expectedTotalCostWithCommission = new BigDecimal(111.1);
    Assert.assertEquals(expectedTotalCostWithCommission,
            fangPortfolio.getCostBasis(validDateForTrading));

    Assert.assertEquals(expectedTotalCostWithCommission,
            fangPortfolio.getValue(validDateForTrading));

    BigDecimal actualCostWithCommission = getTotalCostOfPurchase(sharePurchaseOrders);
    Assert.assertEquals(expectedTotalCostWithCommission, actualCostWithCommission);
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
