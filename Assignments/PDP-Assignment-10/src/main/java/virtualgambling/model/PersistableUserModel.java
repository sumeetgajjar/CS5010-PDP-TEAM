package virtualgambling.model;

import java.io.IOException;

import virtualgambling.model.persistence.Loader;
import virtualgambling.model.persistence.Persister;
import virtualgambling.model.stockdao.StockDAO;

/**
 * This interface represents an enhancement over {@link EnhancedUserModel}. It implements {@link
 * EnhancedUserModel} interface. It adds the ability to persist and a given portfolio to a file and
 * to load it back into the model.
 */
public interface PersistableUserModel extends EnhancedUserModel {

  //  /**
//   * Saves the given portfolio in a file at the give path.
//   *
//   * @param portfolioName the portfolio to save
//   * @param path          the path of the file to save portfolio
//   * @throws IOException if unable to save the portfolio to file
//   */
//  void savePortfolioToFile(String portfolioName, Path path) throws IOException;
//
//  /**
//   * Load the portfolio from the file at the given path into this Model.
//   *
//   * @param path the path of the file
//   * @throws IOException if unable to read from the file
//   */
//  void loadPortfolioFromFileInModel(Path path) throws IOException;
//
//  void loadStrategy(Path path) throws IOException;
//
//  void saveStrategy(Strategy strategy, Path path) throws IOException;

  void persistFromModel(Persister persister) throws IOException;

  void loadIntoModel(Loader loader) throws IOException;

  StockDAO getStockDAO();

  void setStockDao(StockDAO stockDao);
}
