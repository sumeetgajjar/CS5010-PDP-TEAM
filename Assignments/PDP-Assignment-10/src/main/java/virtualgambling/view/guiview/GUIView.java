package virtualgambling.view.guiview;

import virtualgambling.controller.TradingFeatures;
import virtualgambling.view.View;

/**
 * This class represents an GUI View of the MVC application. It extends the traditional {@link View}
 * interface and adds functionality of reporting errors and a way to execute callbacks of
 * controller.
 */
public interface GUIView extends View {

  /**
   * Sets the given TradingFeatures for this View.
   *
   * @param tradingFeatures the feature for this view
   */
  void addFeatures(TradingFeatures tradingFeatures);

  /**
   * Displays the given message as error to the user.
   *
   * @param message the given error message
   */
  void displayError(String message);
}
