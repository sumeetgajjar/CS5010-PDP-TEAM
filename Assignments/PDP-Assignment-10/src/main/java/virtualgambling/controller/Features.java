package virtualgambling.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.factory.StockDataSourceType;

/**
 * TODO
 */
public interface Features {

  boolean createPortfolio(String portfolio);

  Optional<Portfolio> getPortfolio(String portfolioName);

  List<Portfolio> getAllPortfolios();

  Optional<BigDecimal> getPortfolioCostBasis(String portfolio, Date date);

  Optional<BigDecimal> getPortfolioValue(String portfolio, Date date);

  Optional<Portfolio> getPortfolioComposition(String portfolio);

  BigDecimal getRemainingCapital();

  Optional<SharePurchaseOrder> buyShares(String tickerName,
                                         String portfolioName,
                                         Date date,
                                         long quantity,
                                         double commissionPercentage);

  Optional<List<SharePurchaseOrder>> buyShares(String portfolioName,
                                               Date date,
                                               Set<String> tickerNames,
                                               BigDecimal amountToInvest,
                                               double commission
  );

  Optional<List<SharePurchaseOrder>> buyShares(String portfolioName,
                                               Date startDate,
                                               Map<String, Double> stockWeights,
                                               BigDecimal amountToInvest,
                                               double commission
  );

  Optional<List<SharePurchaseOrder>> buyShares(String portfolioName,
                                               Date startDate,
                                               int dayFrequency,
                                               Set<String> tickerNames,
                                               BigDecimal amountToInvest,
                                               double commission
  );

  Optional<List<SharePurchaseOrder>> buyShares(String portfolioName,
                                               Date startDate,
                                               Date endDate,
                                               int dayFrequency,
                                               Set<String> tickerNames,
                                               BigDecimal amountToInvest,
                                               double commission
  );

  Optional<List<SharePurchaseOrder>> buyShares(String portfolioName,
                                               Date startDate,
                                               int dayFrequency,
                                               Map<String, Double> stockWeights,
                                               BigDecimal amountToInvest,
                                               double commission
  );

  Optional<List<SharePurchaseOrder>> buyShares(String portfolioName,
                                               Date startDate,
                                               Date endDate,
                                               int dayFrequency,
                                               Map<String, Double> stockWeights,
                                               BigDecimal amountToInvest,
                                               double commission
  );

  boolean loadAndExecuteStrategy(String portfolioName, String filePath, BigDecimal amountToInvest
          , double commissionPercentage);

  boolean saveStrategy(String filePath, Date startDate, int dayFrequency, Set<String> tickerNames);

  boolean saveStrategy(String filePath, Date startDate, Date endDate, int dayFrequency,
                       Set<String> tickerNames);

  boolean saveStrategy(String filePath, Date startDate, int dayFrequency,
                       Map<String, Double> stockWeights);

  boolean saveStrategy(String filePath, Date startDate, Date endDate, int dayFrequency,
                       Map<String, Double> stockWeights);

  boolean loadPortfolio(String filePath);

  boolean savePortfolio(String portfolioName, String filePath);

  boolean setDataSource(StockDataSourceType stockDataSourceType);

  void quit();
}
