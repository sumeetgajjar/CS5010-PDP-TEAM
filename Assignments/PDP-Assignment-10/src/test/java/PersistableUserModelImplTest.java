import com.google.gson.reflect.TypeToken;

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
import virtualgambling.model.factory.StockDAOType;
import virtualgambling.model.factory.StockDataSourceType;
import virtualgambling.model.persistence.PortfolioLoader;
import virtualgambling.model.persistence.PortfolioPersister;
import virtualgambling.model.persistence.serdes.JSONSerDes;

public class PersistableUserModelImplTest extends EnhancedUserModelTest {
  private static final String PORTFOLIO_P1 = "p1";
  private static final String PORTFOLIO_FANG = "FANG";

  @Override
  protected UserModel getUserModel(StockDAOType stockDAOType,
                                   StockDataSourceType stockDataSourceType) {
    return TestUtils.getEmptyPersistableUserModelWithStockDAO(stockDAOType, stockDataSourceType);
  }

  @Test
  public void persistPortfolioTest() throws IOException {
    PersistableUserModel userModel = TestUtils.getEmptyPersistableUserModel();

    userModel.createPortfolio(PORTFOLIO_P1);

    Path test = Utils.getPathInDefaultFolder(Paths.get(PORTFOLIO_P1 + ".json"));
    JSONSerDes<Portfolio> serDes = new JSONSerDes<>(test, new TypeToken<Portfolio>() {
    }.getType());

    userModel.buyShares("AAPL", PORTFOLIO_P1, TestUtils.getValidDateForTrading(), 10);

    Portfolio portfolio = userModel.getPortfolio(PORTFOLIO_P1);

    try {
      userModel.persistFromModel(new PortfolioPersister(serDes,
              portfolio));
    } catch (IOException e) {
      Assert.fail();
    }

    try {
      userModel.loadIntoModel(new PortfolioLoader(userModel, serDes, PORTFOLIO_P1));
    } catch (IOException e) {
      Assert.fail();
    }

    Assert.assertEquals(userModel.getPortfolio(PORTFOLIO_P1), portfolio);

    userModel.buyShares("GOOG",
            PORTFOLIO_P1,
            TestUtils.getValidDateForTrading(),
            10);

    Assert.assertNotEquals(userModel.getPortfolio(PORTFOLIO_P1), portfolio);

    try {
      userModel.loadIntoModel(new PortfolioLoader(userModel, serDes, PORTFOLIO_P1));
    } catch (IOException e) {
      Assert.fail();
    }

    Assert.assertEquals(userModel.getPortfolio(PORTFOLIO_P1), portfolio);
  }

  @Test
  public void loadingPortfolioFromFileWorks() throws IOException {
    PersistableUserModel userModel = TestUtils.getEmptyPersistableUserModel();

    userModel.createPortfolio("p2");

    userModel.buyShares("GOOG", "p2", TestUtils.getValidDateForTrading(), 10);

    Portfolio portfolio = userModel.getPortfolio("p2");
    Path test = Utils.getPathInDefaultFolder(Paths.get("p2" + ".json"));
    JSONSerDes<Portfolio> serDes = new JSONSerDes<>(test, new TypeToken<Portfolio>() {
    }.getType());

    try {
      userModel.persistFromModel(new PortfolioPersister(serDes,
              portfolio));
    } catch (IOException e) {
      Assert.fail();
    }

    //reinitializing
    userModel = TestUtils.getEmptyPersistableUserModel();

    try {
      userModel.loadIntoModel(new PortfolioLoader(userModel, serDes, "p2"));
    } catch (IOException e) {
      Assert.fail();
    }

    Assert.assertEquals("p2", userModel.getPortfolio("p2").getName());
    Assert.assertEquals(1, userModel.getPortfolio("p2").getPurchases().size());
  }
}