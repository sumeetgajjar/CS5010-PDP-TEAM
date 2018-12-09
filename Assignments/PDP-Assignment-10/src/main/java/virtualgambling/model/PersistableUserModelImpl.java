package virtualgambling.model;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import util.Utils;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.bean.SharePurchaseOrder;
import virtualgambling.model.exceptions.InsufficientCapitalException;
import virtualgambling.model.exceptions.PortfolioNotFoundException;
import virtualgambling.model.exceptions.StockDataNotFoundException;
import virtualgambling.model.exceptions.StrategyExecutionException;
import virtualgambling.model.factory.StockDAOType;
import virtualgambling.model.factory.StockDataSourceType;
import virtualgambling.model.persistence.Loader;
import virtualgambling.model.persistence.Persister;
import virtualgambling.model.strategy.Strategy;

/**
 * This class represents an enhancement over {@link EnhancedUserModelImpl}. It implements {@link
 * PersistableUserModel} interface. It adds the ability to persist and a given portfolio to a file
 * and to load it back into the model.
 */
public class PersistableUserModelImpl extends EnhancedUserModelImpl
        implements PersistableUserModel {

  /**
   * Constructs a instance of {@link PersistableUserModelImpl} with the given params.
   *
   * @param stockDAOType        the stock DAO type
   * @param stockDataSourceType the stockDataSource type
   * @throws IllegalArgumentException if any of the given params are null
   */
  public PersistableUserModelImpl(StockDAOType stockDAOType,
                                  StockDataSourceType stockDataSourceType)
          throws IllegalArgumentException {
    super(stockDAOType, stockDataSourceType);
  }

  @Override
  public void persistFromModel(Persister persister) throws IOException {
    persister.persist();
  }

  @Override
  public void loadIntoModel(Loader loader) throws IOException {
    loader.load(new InnerDecorator(this));
  }

  @Override
  public StockDAOType getStockDAOType() {
    return this.stockDAOType;
  }

  @Override
  public StockDataSourceType getStockDataSourceType() {
    return this.stockDataSourceType;
  }

  @Override
  public void setStockDAOType(StockDAOType stockDAOType) {
    this.stockDAOType = Utils.requireNonNull(stockDAOType);
  }

  @Override
  public void setStockDataSourceType(StockDataSourceType stockDataSourceType) {
    this.stockDataSourceType = Utils.requireNonNull(stockDataSourceType);
  }

  private class InnerDecorator implements PersistableUserModel {
    private final PersistableUserModelImpl delegate;

    private InnerDecorator(PersistableUserModelImpl persistableUserModel) {
      this.delegate = persistableUserModel;
    }

    @Override
    public void createPortfolio(String portfolioName) throws IllegalArgumentException {
      Utils.requireNonNull(portfolioName);
      delegate.validateAndCreatePortfolio(portfolioName);
    }

    @Override
    public Portfolio getPortfolio(String portfolioName) throws PortfolioNotFoundException {
      return delegate.getPortfolio(portfolioName);
    }

    @Override
    public List<Portfolio> getAllPortfolios() {
      return delegate.getAllPortfolios();
    }

    @Override
    public BigDecimal getRemainingCapital() {
      return delegate.getRemainingCapital();
    }

    @Override
    public void persistFromModel(Persister persister) throws IOException {
      delegate.persistFromModel(persister);
    }

    @Override
    public void loadIntoModel(Loader loader) throws IOException {
      delegate.loadIntoModel(loader);
    }

    @Override
    public StockDAOType getStockDAOType() {
      return delegate.getStockDAOType();
    }

    @Override
    public StockDataSourceType getStockDataSourceType() {
      return delegate.getStockDataSourceType();
    }

    @Override
    public void setStockDAOType(StockDAOType stockDAOType) {
      delegate.setStockDAOType(stockDAOType);
    }

    @Override
    public void setStockDataSourceType(StockDataSourceType stockDataSourceType) {
      delegate.setStockDataSourceType(stockDataSourceType);
    }

    @Override
    public SharePurchaseOrder buyShares(String tickerName, String portfolioName, Date date,
                                        long quantity, double commissionPercentage) throws
            IllegalArgumentException, StockDataNotFoundException, InsufficientCapitalException {
      return delegate.buyShares(tickerName, portfolioName, date, quantity, commissionPercentage);
    }

    @Override
    public SharePurchaseOrder buyShares(String tickerName, String portfolioName, Date date,
                                        long quantity) throws IllegalArgumentException,
            StockDataNotFoundException, InsufficientCapitalException {
      return delegate.buyShares(tickerName, portfolioName, date, quantity);
    }

    @Override
    public List<SharePurchaseOrder> buyShares(String portfolioName, BigDecimal amountToInvest,
                                              Strategy strategy, double commissionPercentage)
            throws IllegalArgumentException, StockDataNotFoundException,
            InsufficientCapitalException, StrategyExecutionException {
      return delegate.buyShares(portfolioName, amountToInvest, strategy, commissionPercentage);
    }
  }
}
