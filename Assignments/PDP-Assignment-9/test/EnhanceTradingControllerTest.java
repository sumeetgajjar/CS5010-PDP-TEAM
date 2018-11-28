import util.TestUtils;

/**
 * The class represents a Junit class to test the trading Controller.
 */
public class EnhanceTradingControllerTest extends TradingControllerModelTest {

  @Override
  protected String getMenuStringOfController() {
    return TestUtils.getMenuMessageOfEnhanceTradingController();
  }
}
