package virtualgambling.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import util.Utils;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.InsufficientCapitalException;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.exceptions.StrategyExecutionException;
import virtualgambling.model.factory.StockDAOType;
import virtualgambling.model.factory.StockDataSourceType;
import virtualgambling.model.strategy.Strategy;

/**
 * {@link EnhancedUserModelImpl} is an extension of the {@link SimpleUserModel} class and adds
 * features that allow the user to buy shares and specify commission value. It also provides the
 * option to buy stock via a higher level strategy {@link Strategy}.
 */
public class EnhancedUserModelImpl extends SimpleUserModel implements EnhancedUserModel {

  /**
   * Constructs a Object of {@link EnhancedUserModelImpl} with the given params.
   *
   * @param stockDAOType        the stock DAO type
   * @param stockDataSourceType the stockDataSource type
   * @throws IllegalArgumentException if any of the given params are null
   */
  public EnhancedUserModelImpl(StockDAOType stockDAOType,
                               StockDataSourceType stockDataSourceType) throws IllegalArgumentException {
    super(stockDAOType, stockDataSourceType);
  }

  @Override
  public SharePurchaseOrder buyShares(String tickerName, String portfolioName, Date date,
                                      long quantity, double commissionPercentage) throws
          IllegalArgumentException, StockDataNotFoundException, InsufficientCapitalException {

    SharePurchaseOrder sharePurchaseOrder = new SharePurchaseOrder(createPurchaseOrder(tickerName,
            portfolioName, date, quantity),
            commissionPercentage);

    return processPurchaseOrder(portfolioName, sharePurchaseOrder);
  }

  /**
   * Allows a user to buy stocks into a given portfolio using a {@link Strategy}. In case the
   * portfolio is not found, then it will be created instead of throwing an exception
   *
   * @param portfolioName        portfolio into which to buy the stock
   * @param amountToInvest       amount to invest in each transaction that the strategy executes
   * @param strategy             the strategy with which to purchase stock
   * @param commissionPercentage the percentage out of 100 of the final amount to be added as
   *                             commission charges.
   * @throws StrategyExecutionException in case the user is unable to buy even a single stock
   */
  @Override
  public List<SharePurchaseOrder> buyShares(String portfolioName, BigDecimal amountToInvest,
                                            Strategy strategy,
                                            double commissionPercentage) throws
          IllegalArgumentException, StockDataNotFoundException, InsufficientCapitalException,
          StrategyExecutionException {
    createPortfolioIfNotExists(portfolioName);
    Utils.requireNonNull(strategy);
    this.validateAmountToInvest(amountToInvest);
    return strategy.execute(amountToInvest, this.stockDAOType, this.stockDataSourceType).stream()
            .map(oldPurchaseOrder -> new SharePurchaseOrder(oldPurchaseOrder, commissionPercentage))
            .map(sharePurchaseOrder -> processPurchaseOrder(portfolioName, sharePurchaseOrder))
            .collect(Collectors.toList());
  }

  private void validateAmountToInvest(BigDecimal amountToInvest) throws IllegalArgumentException {
    Utils.requireNonNull(amountToInvest);
    if (amountToInvest.compareTo(BigDecimal.valueOf(1)) <= 0) {
      throw new IllegalArgumentException("Investment amount cannot be less than 1");
    }
  }

  private void createPortfolioIfNotExists(String portfolioName) {
    Utils.requireNonNull(portfolioName);
    if (!this.portfolios.containsKey(portfolioName)) {
      this.createPortfolio(portfolioName);
    }
  }
}
