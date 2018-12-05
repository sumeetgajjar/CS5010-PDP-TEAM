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

  //  /**
//   * Saves the given portfolio in a file at the give path.
//   *
//   * @param portfolioName the portfolio to save
//   * @param path          the path of the file to save portfolio
//   * @throws IOException              if unable to save the portfolio to file
//   * @throws IllegalArgumentException if the any of the given params are null or if the portfolio
//   *                                  does not exists in this model
//   */
//  @Override
//  public void savePortfolioToFile(String portfolioName, Path path) throws IOException,
//          IllegalArgumentException {
//    Portfolio portfolio = getPortfolio(portfolioName);
//    String serializedString = portfolio.serialize();
//    Utils.saveToFile(path, serializedString);
//  }
//
//  //todo use a specific exception
//
//  /**
//   * Load the portfolio from the file at the given path into this Model. It throws {@link
//   * IllegalStateException} if a portfolio already exists with the name as same the name of the
//   * portfolio which is being loaded from the file.
//   *
//   * @param path the path of the file
//   * @throws IOException              if unable to read from the file
//   * @throws IllegalArgumentException if the given path is null
//   */
//  @Override
//  public void loadPortfolioFromFileInModel(Path path) throws IOException,
// IllegalArgumentException,
//          IllegalStateException {
//    String serializedString = Utils.readStringFromFile(path);
//    Portfolio portfolio = Portfolio.deserialize(serializedString);
//    if (this.portfolios.containsKey(portfolio.getName())) {
//      throw new IllegalStateException("Cannot load portfolio into model");
//    }
//    this.portfolios.put(portfolio.getName(), portfolio);
//  }
}
