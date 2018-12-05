package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 11:43 PM, 12/4/18
 */
public class CreatePortfolioForm extends AbstractForm {

  private final MainForm mainForm;
  private final Features features;

  public CreatePortfolioForm(MainForm mainForm, Features features) throws HeadlessException {
    super(mainForm);
    this.mainForm = mainForm;
    this.features = features;
  }

  @Override
  protected void initComponents() {
    JPanel jPanel = new JPanel();

    JLabel jLabel = new JLabel("Please enter the name of the Portfolio");
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
      if (this.isTextFieldEmpty(portfolioNameJTextField)) {
        this.showError("Portfolio Name cannot be empty");
        return;
      }

//      this.features.createPortfolio(portfolioNameJTextField.getText());
      //todo insert command here

      this.appendOutput("Create portfolio");
      this.showPrevious();
    };
  }
}