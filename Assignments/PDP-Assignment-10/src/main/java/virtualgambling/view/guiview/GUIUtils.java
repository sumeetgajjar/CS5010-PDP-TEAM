package virtualgambling.view.guiview;

import javax.swing.*;

/**
 * This class contains utility functions related to GUI, which can be used by any class.
 */
public class GUIUtils {

  /**
   * Displays a pop-up containing the given error message.
   *
   * @param jFrame  the parent JFrame
   * @param message the message to be displayed
   */
  public static void displayError(JFrame jFrame, String message) {
    JOptionPane.showMessageDialog(jFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Hides the current JFrame and displays the previous JFrame.
   *
   * @param previous the previous JFrame
   * @param current  the current JFrame
   */
  public static void showPrevious(JFrame previous, JFrame current) {
    current.setVisible(false);
    previous.setVisible(true);
  }

  /**
   * Displays a prompt to the User to enter an input.
   *
   * @param jFrame the parent JFrame
   * @return the input from the user
   */
  public static String getInput(JFrame jFrame) {
    return JOptionPane.showInputDialog(jFrame, "Please enter the input");
  }
}
