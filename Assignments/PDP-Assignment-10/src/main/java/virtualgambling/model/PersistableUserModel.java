package virtualgambling.model;

import java.io.IOException;

import virtualgambling.model.factory.StockDAOType;
import virtualgambling.model.factory.StockDataSourceType;
import virtualgambling.model.persistence.Loader;
import virtualgambling.model.persistence.Persister;

/**
 * This interface represents an enhancement over {@link PersistableUserModel}. It implements {@link
 * PersistableUserModel} interface. It adds the ability to persist and a given portfolio to a file
 * and to load it back into the model.
 */
public interface PersistableUserModel extends EnhancedUserModel {

  void persistFromModel(Persister persister) throws IOException;

  void loadIntoModel(Loader loader) throws IOException;

  StockDAOType getStockDAOType();

  StockDataSourceType getStockDataSourceType();

  void setStockDAOType(StockDAOType stockDAOType);

  void setStockDataSourceType(StockDataSourceType stockDataSourceType);
}
