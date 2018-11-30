import util.TestUtils;
import virtualgambling.model.UserModel;

/**
 * The class represents a Junit test class. It contains all the tests for scenarios when
 * TradingController is given EnhancedUserModel.
 */
public class TradingControllerEnhancedUserModelTest extends TradingControllerSimpleUserModelTest {

  @Override
  protected UserModel getUserModel() {
    return TestUtils.getEmptyEnhancedUserModel();
  }
}
