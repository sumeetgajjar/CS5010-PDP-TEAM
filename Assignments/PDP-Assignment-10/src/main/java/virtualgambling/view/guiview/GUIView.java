package virtualgambling.view.guiview;

import virtualgambling.controller.Features;
import virtualgambling.view.View;

/**
 * This class represents an GUI View of the MVC application. It extends the traditional {@link View}
 * interface and adds functionality of reporting errors and a way to execute callbacks of
 * controller.
 */
public interface GUIView extends View {

  /**
   * Sets the given Features, available for this View.
   *
   * @param features the feature available to this view
   */
  void addFeatures(Features features);

  /**
   * Displays the given message as error to the user.
   *
   * @param message the given error message
   */
  void displayError(String message);
}
