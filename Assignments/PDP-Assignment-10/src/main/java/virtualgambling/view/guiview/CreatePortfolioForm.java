package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Created by gajjar.s, on 11:43 PM, 12/4/18
 */
public class CreatePortfolioForm extends AbstractForm {

  private final MainForm mainForm;

  public CreatePortfolioForm(MainForm mainForm) throws HeadlessException {
    super(mainForm);
    this.mainForm = mainForm;
  }

  @Override
  protected void initComponents() {
    JPanel jPanel = new JPanel();

    JLabel jLabel = new JLabel("Please enter the name of the Portfolio");
    jLabel.setHorizontalTextPosition(SwingConstants.CENTER);
    jPanel.add(jLabel);

    JTextField portfolioNameJTextField = new JTextField(10);
    jPanel.add(portfolioNameJTextField);

    ActionListener actionListener = getActionListenerForCreatePortfolio(portfolioNameJTextField);
    JPanel buttonJPanel = this.getActionButtonJPanel(actionListener);

    this.addJFrameClosingEvent();

    jPanel.add(buttonJPanel);
    this.add(jPanel);
  }

  @Override
  protected void appendOutput(String message) {
    this.mainForm.appendOutput(message);
  }

  private ActionListener getActionListenerForCreatePortfolio(JTextField portfolioNameJTextField) {
    return e -> {
      String portfolioName = portfolioNameJTextField.getText();
      if (portfolioName.isEmpty()) {
        this.showError("Portfolio Name cannot be empty");
        return;
      }

      this.appendOutput(portfolioName);
      this.showPrevious();
    };
  }

  public static void main(String[] args) {
    new CreatePortfolioForm(null).setVisible(true);
  }
}
