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
 * {@link TradingFeatures} represents a list of tradingFeatures that a GUI view uses in it's
 * callback in order to simulate a Command. This ensures that the Controller and View remain
 * decoupled.
 */
public interface TradingFeatures {

  /**
   * Creates a portfolio for this UserModel and returns true if successfully created, false
   * otherwise.
   *
   * @param portfolio the name of the portfolio
   * @return true if the portfolio is created successfully, false otherwise
   */
  boolean createPortfolio(String portfolio);

  /**
   * Gets the portfolio matching portfolioName.
   *
   * @param portfolioName name of the portfolio
   * @return gets the portfolio matching portfolioName if present
   */
  Optional<Portfolio> getPortfolio(String portfolioName);

  /**
   * Returns a list of immutable Portfolios
   *
   * @return a list of portfolios
   */
  List<Portfolio> getAllPortfolios();

  /**
   * Returns the costBasis of this portfolio at the given date.
   *
   * @param date the date
   * @return the costBasis of the given portfolio if possible
   */
  Optional<BigDecimal> getPortfolioCostBasis(String portfolio, Date date);

  /**
   * Returns the total value of this portfolio at the given date.
   *
   * @param date the date
   * @return the total value of the portfolio
   */
  Optional<BigDecimal> getPortfolioValue(String portfolio, Date date);

  /**
   * Returns the amount of liquid cash that is remaining with the user.
   *
   * <p>The amount that a user starts with is implementation dependent.
   *
   * @return the amount of liquid cash that is remaining with the user.
   */
  BigDecimal getRemainingCapital();

  /**
   * Buys shares with given the ticker name, date, and quantity into the given portfolio.
   *
   * @param tickerName           the ticker name of the stock to buy
   * @param portfolioName        the portfolio in which the stock needs to be purchased
   * @param date                 the date at which the stock is to be purchased
   * @param quantity             the quantity of the stock to buy
   * @param commissionPercentage the commission percentage per transaction
   * @return Optional containing the SharePurchaseOrder if the share was purchased successfully
   */
  Optional<SharePurchaseOrder> buyShares(String tickerName,
                                         String portfolioName,
                                         Date date,
                                         long quantity,
                                         double commissionPercentage);

  /**
   * Buys shares of the given ticker name in equal proportions on the given date into the given
   * portfolio.
   *
   * @param portfolioName  the portfolio in which the stock needs to be purchased
   * @param date           the date at which the stock is to be purchased
   * @param tickerNames    the ticker names of stocks to be purchased
   * @param amountToInvest the total amount to invest
   * @param commission     the commission percentage per transaction
   * @return Optional containing the List of purchases if it was purchased successfully
   */
  Optional<List<SharePurchaseOrder>> buyShares(String portfolioName,
                                               Date date,
                                               Set<String> tickerNames,
                                               BigDecimal amountToInvest,
                                               double commission
  );

  /**
   * Buys shares of the given ticker name in given proportions on the given date into the given
   * portfolio.
   *
   * @param portfolioName  the portfolio in which the stock needs to be purchased
   * @param startDate      the date at which the stock is to be purchased
   * @param stockWeights   percentages of stocks to purchase
   * @param amountToInvest the total amount to invest
   * @param commission     the commission percentage per transaction
   * @return Optional containing the List of purchases if it was purchased successfully
   */
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
