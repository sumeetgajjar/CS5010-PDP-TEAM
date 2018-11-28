import org.junit.Before;

import util.TestUtils;
import virtualgambling.controller.Controller;
import virtualgambling.controller.EnhancedTradingController;
import virtualgambling.model.EnhancedUserModel;
import virtualgambling.model.UserModel;
import virtualgambling.view.TextView;

/**
 * The class represents a Junit test class.It contains all the tests for scenarios when
 * EnhancedTradingController is given EnhancedUserModel.
 */
public class EnhancedTradingControllerEnhancedModelTest extends ControllerModelTest {

  private EnhancedUserModel enhancedUserModel;

  @Before
  public void setUp() {
    this.enhancedUserModel = TestUtils.getEmptyEnhancedUserModel();
    this.userModel = enhancedUserModel;
  }

  @Override
  protected UserModel getUserModel() {
    return userModel;
  }

  @Override
  protected Controller getController(Readable readable,
                                     Appendable appendable) {
    return new EnhancedTradingController(enhancedUserModel,
            new TextView(readable, appendable));
  }

  @Override
  protected String getMenuStringOfController() {
    return TestUtils.getMenuMessageOfEnhanceTradingController();
  }
}
