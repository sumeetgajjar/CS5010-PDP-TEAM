package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 11:43 PM, 12/4/18
 */
public class CreatePortfolioForm extends AbstractForm {

  protected final MainForm mainForm;
  protected final Features features;

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

    jPanel.add(buttonJPanel);
    this.add(jPanel);
  }

  protected ActionListener getActionListenerForCreatePortfolio(JTextField portfolioNameJTextField) {
    return e -> {
      if (this.isPortfolioNameTextFieldEmpty(portfolioNameJTextField)) {
        return;
      }

      String portfolioName = portfolioNameJTextField.getText();
      boolean success = this.features.createPortfolio(portfolioName);
      if (success) {
        this.showPrevious();
        this.mainForm.display(String.format("Portfolio '%s' Successfully Created", portfolioName));
      }
    };
  }
}
