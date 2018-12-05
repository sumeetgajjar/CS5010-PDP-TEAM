package virtualgambling.view.guiview;

import javax.swing.*;

/**
 * Created by gajjar.s, on 12:11 AM, 12/5/18
 */
public class GUIUtils {

  public static JButton getQuitJButton() {
    JButton quitButton = new JButton("Quit");
    quitButton.addActionListener(e -> System.exit(0));
    return quitButton;
  }

  public static void displayError(JFrame jFrame, String message) {
    JOptionPane.showMessageDialog(jFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
  }

  public static void showPrevious(JFrame previous, JFrame current) {
    current.setVisible(false);
    previous.setVisible(true);
  }
}
