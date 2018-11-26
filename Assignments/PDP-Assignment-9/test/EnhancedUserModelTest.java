import util.TestUtils;
import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.EnhancedUserModelImpl;
import virtualgambling.model.UserModel;
import virtualgambling.model.stockdao.StockDAO;

public class EnhancedUserModelTest extends UserModelTest {
  @Override
  protected UserModel getUserModel(StockDAO stockDAO) {
    return new EnhancedUserModelImpl(stockDAO);
  }

  private EnhancedUserModel getEnhancedUserModel(StockDAO stockDAO) {
    return TestUtils.getEmptyEnhancedUserModelWithStockDAO(stockDAO);
  }
}
