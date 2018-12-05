package virtualgambling.view.guiview;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

/**
 * Created by gajjar.s, on 12:11 AM, 12/5/18
 */
public class GUIUtils {

  public static JPanel getActionButtonJPanel(JFrame currentJFrame,
                                             JFrame previousJFrame,
                                             ActionListener executeButtonAction) {
    JPanel buttonJPanel = new JPanel();
    buttonJPanel.setLayout(new BoxLayout(buttonJPanel, BoxLayout.X_AXIS));

    JButton executeJButton = new JButton("Execute");
    executeJButton.addActionListener(executeButtonAction);

    JButton cancelJButton = new JButton("Cancel");
    cancelJButton.addActionListener(e -> GUIUtils.showPrevious(previousJFrame, currentJFrame));

    buttonJPanel.add(executeJButton);
    buttonJPanel.add(cancelJButton);
    return buttonJPanel;
  }

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

  public static void addJFrameClosingEvent(JFrame jFrame,
                                           JFrame previousJFrame,
                                           JFrame currentJFrame) {

    jFrame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        showPrevious(previousJFrame, currentJFrame);
      }
    });
  }
}
