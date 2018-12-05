import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import util.TestUtils;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.persistence.PortfolioPersister;
import virtualgambling.model.persistence.serdes.JSONSerDes;
import virtualgambling.model.stockdao.StockDAO;

public class PersistableUserModelImplTest extends EnhancedUserModelTest {
  private static final String PORTFOLIO_P1 = "p1";
  private static final String PORTFOLIO_FANG = "FANG";

  @Override
  protected UserModel getUserModel(StockDAO stockDAO) {
    return TestUtils.getEmptyPersistableUserModelWithStockDAO(stockDAO);
  }

  @Test
  public void persistPortfolioTest() {
    PersistableUserModel userModel = TestUtils.getEmptyPersistableUserModel();

    userModel.createPortfolio(PORTFOLIO_P1);

    Path test = Paths.get("test");

    try {
      userModel.persistFromModel(new PortfolioPersister(new JSONSerDes<>(test),
              userModel.getPortfolio(PORTFOLIO_P1), test));
    } catch (IOException e) {
      Assert.fail();
    }
  }
}