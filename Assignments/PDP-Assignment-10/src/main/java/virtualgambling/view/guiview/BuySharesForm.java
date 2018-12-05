package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Created by gajjar.s, on 11:43 PM, 12/4/18
 */
public class BuySharesForm extends AbstractForm {

  private final MainForm mainForm;

  public BuySharesForm(MainForm mainForm) throws HeadlessException {
    super(mainForm);
    this.mainForm = mainForm;
  }

  @Override
  protected void initComponents() {

    this.setLayout(new GridLayout(6, 1));

    JPanel tickerNamePanel = new JPanel();
    JLabel tickerNameLabel = new JLabel("Please enter the ticker name of the stock");
    tickerNamePanel.add(tickerNameLabel);

    JTextField tickerNameTextField = new JTextField(10);
    tickerNamePanel.add(tickerNameTextField);

    JPanel portfolioPanel = new JPanel();
    JLabel portfolioLabel = new JLabel("Please enter the name of the Portfolio");
    portfolioPanel.add(portfolioLabel);

    JTextField portfolioTextField = new JTextField(10);
    portfolioPanel.add(portfolioTextField);

    JPanel datePanel = new JPanel();
    JLabel dateLabel = new JLabel("Please enter the date (YYYY-MM-DD)");
    datePanel.add(dateLabel);

    JTextField dateTextField = new JTextField(10);
    datePanel.add(dateTextField);

    JPanel quantityPanel = new JPanel();
    JLabel quantityLabel = new JLabel("Please enter quantity of shares to buy");
    quantityPanel.add(quantityLabel);

    JTextField quantityTextField = new JTextField(10);
    quantityPanel.add(quantityTextField);

    JPanel commissionPanel = new JPanel();
    JLabel commissionLabel = new JLabel("Please enter quantity of shares to buy");
    commissionPanel.add(commissionLabel);

    JTextField commissionTextField = new JTextField(10);
    commissionPanel.add(commissionTextField);

    ActionListener actionListener = getActionListenerForCreatePortfolio(
            tickerNameTextField,
            portfolioTextField,
            dateTextField,
            quantityTextField,
            commissionTextField);

    JPanel buttonJPanel = this.getActionButtonJPanel(actionListener);

    this.addJFrameClosingEvent();

    this.add(tickerNamePanel);
    this.add(portfolioPanel);
    this.add(datePanel);
    this.add(quantityPanel);
    this.add(commissionPanel);
    this.add(buttonJPanel);
  }

  @Override
  protected void appendOutput(String message) {
    this.mainForm.appendOutput(message);
  }

  private ActionListener getActionListenerForCreatePortfolio(JTextField tickerNameTextField,
                                                             JTextField portfolioTextField,
                                                             JTextField dateTextField,
                                                             JTextField quantityTextField,
                                                             JTextField commissionTextField) {
    return e -> {
      if (this.isTextFieldEmpty(tickerNameTextField)) {
        this.showError("Ticker Name cannot be empty");
        return;
      }

      if (this.isTextFieldEmpty(portfolioTextField)) {
        this.showError("Portfolio Name cannot be empty");
        return;
      }

      if (this.isTextFieldEmpty(dateTextField)) {
        this.showError("Date cannot be empty");
        return;
      }

      if (this.isTextFieldEmpty(quantityTextField)) {
        this.showError("Quantity cannot be empty");
        return;
      }

      if (this.isTextFieldEmpty(commissionTextField)) {
        this.showError("Commission cannot be empty");
        return;
      }

      //todo insert command here

      this.appendOutput("Buy single share");
      this.showPrevious();
    };
  }

  public static void main(String[] args) {
    new BuySharesForm(null).setVisible(true);
  }
}
