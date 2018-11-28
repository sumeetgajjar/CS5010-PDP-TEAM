package virtualgambling.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import util.Utils;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.InsufficientCapitalException;
import virtualgambling.model.exceptions.StockDataNotFoundException;
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
                                      long quantity, double commissionPercentage) throws IllegalArgumentException, StockDataNotFoundException, InsufficientCapitalException {

    SharePurchaseOrder sharePurchaseOrder = new SharePurchaseOrder(createPurchaseOrder(tickerName,
            portfolioName, date, quantity),
            commissionPercentage);

    return processPurchaseOrder(portfolioName, sharePurchaseOrder);
  }

  @Override
  public List<SharePurchaseOrder> buyShares(String portfolioName, BigDecimal amountToInvest,
                                            Strategy strategy,
                                            double commissionPercentage) throws IllegalArgumentException, StockDataNotFoundException, InsufficientCapitalException {
    createPortfolioIfNotExists(portfolioName);
    return strategy.execute(amountToInvest, this.stockDAO).stream()
            .map(oldPurchaseOrder -> new SharePurchaseOrder(oldPurchaseOrder, commissionPercentage))
            .map(sharePurchaseOrder -> processPurchaseOrder(portfolioName, sharePurchaseOrder))
            .collect(Collectors.toList());
  }

  private void createPortfolioIfNotExists(String portfolioName) {
    Utils.requireNonNull(portfolioName);
    if (!this.portfolios.containsKey(portfolioName)) {
      this.createPortfolio(portfolioName);
    }
  }
}
