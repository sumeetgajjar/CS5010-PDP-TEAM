package virtualgambling.view;

import java.io.IOException;

/**
 * This interface represents a View for our VirtualGambling MVC app. View can take input from User
 * and display text to the user.
 */
public interface View {

  /**
   * Returns the input from the user.
   *
   * @return the input from the user
   * @throws IOException if unable to read the input from user
   */
  String getInput() throws IOException;

  /**
   * Displays the given text to the user.
   *
   * @param text the text to display
   * @throws IOException if unable to display the text to the user
   */
  void display(String text) throws IOException;
}
