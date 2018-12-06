package virtualgambling.model;

import java.io.IOException;

import util.Utils;
import virtualgambling.model.factory.StockDAOType;
import virtualgambling.model.factory.StockDataSourceType;
import virtualgambling.model.persistence.Loader;
import virtualgambling.model.persistence.Persister;

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
  public void createPortfolio(String portfolioName) throws IllegalArgumentException {
    Utils.requireNonNull(portfolioName);
    this.validateAndCreatePortfolio(portfolioName);
  }

  @Override
  public void persistFromModel(Persister persister) throws IOException {
    persister.persist();
  }

  @Override
  public void loadIntoModel(Loader loader) throws IOException {
    loader.load(this);
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
}
