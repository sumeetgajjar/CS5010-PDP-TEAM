package virtualgambling.controller;

import virtualgambling.model.PersistableUserModel;
import virtualgambling.view.View;

public class PersistenceTradingController extends EnhancedTradingController {
  /**
   * Constructs a object of {@link EnhancedTradingController} with the given params.
   *
   * @param enhancedUserModel the userModel
   * @param view              the view
   * @throws IllegalArgumentException if the given params are null
   */
  public PersistenceTradingController(PersistableUserModel enhancedUserModel, View view)
          throws IllegalArgumentException {
    super(enhancedUserModel, view);
  }
}
