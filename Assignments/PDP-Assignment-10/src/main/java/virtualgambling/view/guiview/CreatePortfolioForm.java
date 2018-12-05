package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Created by gajjar.s, on 11:43 PM, 12/4/18
 */
public class CreatePortfolioForm extends JFrame {

  private final JFrame previousJFrame;
  private final JFrame currentJFrame;

  public CreatePortfolioForm(MainForm mainForm) throws HeadlessException {
    this.currentJFrame = this;
    this.previousJFrame = mainForm;

    JPanel jPanel = new JPanel();

    JLabel jLabel = new JLabel("Please enter the name of the Portfolio");
    jLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    jPanel.add(jLabel);

    JTextField portfolioNameJTextField = new JTextField(10);
    jPanel.add(portfolioNameJTextField);

    ActionListener actionListener = getActionListenerForCreatePortfolio(mainForm,
            portfolioNameJTextField);
    JPanel buttonJPanel = GUIUtils.getActionButtonJPanel(this, previousJFrame, actionListener);

    GUIUtils.addJFrameClosingEvent(this, previousJFrame, currentJFrame);

    jPanel.add(buttonJPanel);
    this.add(jPanel);
    this.setLocationRelativeTo(null);
    this.pack();
  }

  private ActionListener getActionListenerForCreatePortfolio(MainForm mainForm,
                                                             JTextField portfolioNameJTextField) {
    return e -> {
      String portfolioName = portfolioNameJTextField.getText();
      if (portfolioName.isEmpty()) {
        GUIUtils.displayError(this, "Portfolio Name cannot be empty!");
        return;
      }

      mainForm.appendOutput(portfolioName);
      GUIUtils.showPrevious(this.previousJFrame, this.currentJFrame);
    };
  }

  public static void main(String[] args) {
    new CreatePortfolioForm(null).setVisible(true);
  }
}
