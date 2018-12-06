package virtualgambling.view.guiview;

import java.awt.HeadlessException;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.Utils;
import virtualgambling.controller.Features;

/**
 * This class represents a GUI form to create a portfolio. It extends {@link AbstractForm} to reduce
 * the effort in implementing the common functionality.
 */
public class CreatePortfolioForm extends AbstractForm {

  protected final MainForm mainForm;
  protected final Features features;

  /**
   * Constructs a object of CreatePortfolioForm with the given params.
   *
   * @param mainForm the mainForm
   * @param features the features
   * @throws IllegalArgumentException if the given params are null
   */
  public CreatePortfolioForm(MainForm mainForm, Features features) throws HeadlessException {
    super(mainForm);
    this.mainForm = Utils.requireNonNull(mainForm);
    this.features = Utils.requireNonNull(features);
    this.setTitle("Portfolio creation");
  }

  @Override
  protected void initComponents() {
    JPanel container = new JPanel();

    JLabel jLabel = new JLabel("Please enter the name of the Portfolio");
    container.add(jLabel);

    JTextField portfolioNameJTextField = new JTextField(10);
    container.add(portfolioNameJTextField);

    ActionListener actionListener = getActionListenerForCreatePortfolio(portfolioNameJTextField);
    JPanel buttonJPanel = this.getActionButtonJPanel(actionListener);

    container.add(buttonJPanel);
    this.add(container);
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
