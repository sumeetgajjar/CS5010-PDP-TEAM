import util.TestUtils;
import virtualgambling.model.UserModel;
import virtualgambling.model.factory.StockDAOType;
import virtualgambling.model.factory.StockDataSourceType;

/**
 * The class represents a Junit class to test Persistable User Model in isolation.
 */
public class PersistableUserModelTest extends EnhancedUserModelTest {

  @Override
  protected UserModel getUserModel(StockDAOType stockDAOType,
                                   StockDataSourceType stockDataSourceType) {
    return TestUtils.getEmptyPersistableUserModelWithStockDAO(stockDAOType, stockDataSourceType);
  }
}
