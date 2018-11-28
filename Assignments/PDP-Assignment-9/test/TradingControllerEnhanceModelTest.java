import util.TestUtils;
import virtualgambling.model.UserModel;

/**
 * The class represents a Junit class to test the trading Controller.
 */
public class TradingControllerEnhanceModelTest extends TradingControllerSimpleUserModelTest {

  @Override
  protected UserModel getUserModel() {
    return TestUtils.getEmptyEnhancedUserModel();
  }
}