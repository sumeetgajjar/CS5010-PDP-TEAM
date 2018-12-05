package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

/**
 * Created by gajjar.s, on 1:50 AM, 12/5/18
 */
public abstract class AbstractForm extends JFrame {

  protected final JFrame previousJFrame;
  protected final JFrame currentJFrame;

  protected AbstractForm(JFrame previousJFrame) {
    this.previousJFrame = previousJFrame;
    this.currentJFrame = this;
    this.initComponents();
    this.postInit();
  }

  protected abstract void initComponents();

  protected void appendOutput(String message) {

  }

  protected void postInit() {
    this.pack();
    this.centerThisJFrame();
  }

  protected JPanel getActionButtonJPanel(ActionListener executeButtonAction) {
    JPanel buttonJPanel = new JPanel();
    buttonJPanel.setLayout(new BoxLayout(buttonJPanel, BoxLayout.X_AXIS));

    JButton executeJButton = new JButton("Execute");
    executeJButton.addActionListener(executeButtonAction);

    JButton cancelJButton = new JButton("Cancel");
    cancelJButton.addActionListener(e -> this.showPrevious());

    buttonJPanel.add(executeJButton);
    buttonJPanel.add(cancelJButton);
    return buttonJPanel;
  }

  protected void showError(String message) {
    GUIUtils.displayError(this, message);
  }

  protected void addJFrameClosingEvent() {

    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        showPrevious();
      }
    });
  }

  protected void showPrevious() {
    GUIUtils.showPrevious(previousJFrame, currentJFrame);
  }

  protected void centerThisJFrame() {
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) (dim.getWidth() / 2 - this.getSize().getWidth() / 2);
    int y = (int) (dim.getHeight() / 2 - this.getSize().getHeight() / 2);
    this.setLocation(x, y);
  }
}
