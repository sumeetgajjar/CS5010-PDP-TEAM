package virtualgambling.model;

import java.io.IOException;

import util.Utils;
import virtualgambling.model.factory.StockDAOType;
import virtualgambling.model.factory.StockDataSourceType;
import virtualgambling.model.persistence.Loader;
import virtualgambling.model.persistence.Persister;

/**
 * Created by gajjar.s, on 12:20 PM, 12/2/18
 */
public class PersistableUserModelImpl extends EnhancedUserModelImpl implements PersistableUserModel {

//  /**
//   * Constructs a {@link PersistableUserModelImpl} object with given params.
//   *
//   * @param stockDAO the stockDAO
//   * @throws IllegalArgumentException if the given stockDAO is null
//   */
//  public PersistableUserModelImpl(StockDAO stockDAO) throws IllegalArgumentException {
//    super(stockDAO);
//  }

  public PersistableUserModelImpl(StockDAOType stockDAOType,
                                  StockDataSourceType stockDataSourceType) throws IllegalArgumentException {
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
    loader.load();
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
