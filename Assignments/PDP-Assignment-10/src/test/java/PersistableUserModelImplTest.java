import com.owlike.genson.GenericType;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import util.TestUtils;
import util.Utils;
import virtualgambling.model.PersistableUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.model.bean.Portfolio;
import virtualgambling.model.persistence.PortfolioLoader;
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
  public void persistPortfolioTest() throws IOException {
    PersistableUserModel userModel = TestUtils.getEmptyPersistableUserModel();

    userModel.createPortfolio(PORTFOLIO_P1);

    Path test = Utils.getPathInDefaultFolder(Paths.get("test.json"));
    JSONSerDes<Portfolio> serDes = new JSONSerDes<>(test, new GenericType<Portfolio>(){});

    try {
      userModel.persistFromModel(new PortfolioPersister(serDes,
              userModel.getPortfolio(PORTFOLIO_P1)));
    } catch (IOException e) {
      Assert.fail();
    }

    try {
      userModel.loadIntoModel(new PortfolioLoader(userModel, serDes, PORTFOLIO_P1));
    } catch (IOException e) {
      Assert.fail();
    }
  }
}