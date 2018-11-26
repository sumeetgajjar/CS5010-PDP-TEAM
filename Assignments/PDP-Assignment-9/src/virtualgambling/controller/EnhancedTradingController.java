package virtualgambling.controller;

import virtualgambling.model.EnhancedUserModel;
import virtualgambling.view.View;

public class EnhancedTradingController extends TradingController {
  /**
   * Constructs a object of {@link TradingController} with the given params.
   *
   * @param enhancedUserModel the userModel
   * @param view              the view
   * @throws IllegalArgumentException if the given params are null
   */
  public EnhancedTradingController(EnhancedUserModel enhancedUserModel, View view) throws IllegalArgumentException {
    super(enhancedUserModel, view);
  }
}
