package virtualgambling.model;

import java.io.IOException;

import virtualgambling.model.factory.StockDAOType;
import virtualgambling.model.factory.StockDataSourceType;
import virtualgambling.model.persistence.Loader;
import virtualgambling.model.persistence.Persister;
import virtualgambling.model.stockdao.StockDAO;
import virtualgambling.model.stockdatasource.StockDataSource;

/**
 * This interface represents an enhancement over {@link PersistableUserModel}. It implements {@link
 * PersistableUserModel} interface. It adds the ability to persist and a given portfolio to a file
 * and to load it back into the model.
 *
 * <p>It also uses the Strategy Design Pattern and allows the user to change the {@link StockDAO}
 * and {@link StockDataSource} at run time.
 */
public interface PersistableUserModel extends EnhancedUserModel {

  /**
   * Given a {@link Persister}, persistFromModel delegates the job of persisting to the {@link
   * Persister}. The model acts as a mediator and takes control of Persisting operations.
   *
   * @param persister persister that serializes and saves the data in the
   * @throws IOException if persistence is not possible.
   */
  void persistFromModel(Persister persister) throws IOException;

  /**
   * Given a {@link Loader}, loadIntoModel delegates the job of deserialization to the loader and
   * then updates it's own state when necessary.
   *
   * @param loader loader that deserializes and loads into the model
   * @throws IOException if loading is not possible
   */
  void loadIntoModel(Loader loader) throws IOException;

  /**
   * Gets the {@link StockDAOType} that the model is currently operating with.
   *
   * @return the {@link StockDAOType} that the model is currently operating with.
   */
  StockDAOType getStockDAOType();

  /**
   * Gets the {@link StockDataSourceType} that the model is currently operating with.
   *
   * @return the {@link StockDataSourceType} that the model is currently operating with.
   */
  StockDataSourceType getStockDataSourceType();

  /**
   * Sets the {@link StockDAOType} that the model should operate with.
   */
  void setStockDAOType(StockDAOType stockDAOType);

  /**
   * Sets the {@link StockDataSourceType} that the model should operate with.
   */
  void setStockDataSourceType(StockDataSourceType stockDataSourceType);
}
