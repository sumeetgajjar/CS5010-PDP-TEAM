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
import virtualgambling.model.stockdao.StockDAO;
import virtualgambling.model.strategy.Strategy;

public class EnhancedUserModelImpl extends SimpleUserModel implements EnhancedUserModel {
  /**
   * Constructs a {@link EnhancedUserModelImpl} object with given params.
   *
   * @param stockDAO the stockDAO
   * @throws IllegalArgumentException if the given stockDAO is null
   */
  public EnhancedUserModelImpl(StockDAO stockDAO) throws IllegalArgumentException {
    super(stockDAO);
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
    return strategy.execute(amountToInvest, this.stockDAO).stream()
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
