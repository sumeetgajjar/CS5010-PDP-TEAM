package virtualgambling.controller;

import virtualgambling.model.UserModelV2;
import virtualgambling.view.View;

public class EnhancedTradingController extends TradingController {
  /**
   * Constructs a object of {@link TradingController} with the given params.
   *
   * @param userModelV2 the userModel
   * @param view        the view
   * @throws IllegalArgumentException if the given params are null
   */
  public EnhancedTradingController(UserModelV2 userModelV2, View view) throws IllegalArgumentException {
    super(userModelV2, view);
  }
}
