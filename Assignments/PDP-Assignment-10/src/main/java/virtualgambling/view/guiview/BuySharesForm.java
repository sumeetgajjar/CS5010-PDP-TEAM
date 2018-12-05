package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;

import javax.swing.*;

import util.Utils;
import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 11:43 PM, 12/4/18
 */
public class BuySharesForm extends AbstractForm {

  private final MainForm mainForm;
  private final Features features;

  public BuySharesForm(MainForm mainForm, Features features) throws HeadlessException {
    super(mainForm);
    this.mainForm = mainForm;
    this.features = features;
  }

  @Override
  protected void appendOutput(String message) {
    this.mainForm.appendOutput(message);
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

      String tickerName = tickerNameTextField.getText();
      String portfolioName = portfolioTextField.getText();
      String dateString = dateTextField.getText();
      Date date;
      try {
        date = Utils.getDateFromDefaultFormattedDateString(dateString);
      } catch (ParseException e1) {
        this.showError(String.format("Invalid Date Format: %s", dateString));
        return;
      }


      long quantity;
      String quantityString = quantityTextField.getText();
      try {
        quantity = Long.parseLong(quantityString);
      } catch (NumberFormatException e1) {
        this.showError(String.format("Invalid Quantity: %s", quantityString));
        return;
      }

      double commission;
      String commissionString = commissionTextField.getText();
      try {
        commission = Double.parseDouble(commissionString);
      } catch (NumberFormatException e1) {
        this.showError(String.format("Invalid Commission: %s", commissionString));
        return;
      }


//      this.features.buyShares(tickerName, portfolioName, date, quantity, commission);
      //todo insert command here

      this.appendOutput("Buy single share");
      this.showPrevious();
    };
  }
}
