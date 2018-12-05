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
public class GetPortfolioCostBasisForm extends AbstractForm {

  private final MainForm mainForm;
  protected final Features features;

  public GetPortfolioCostBasisForm(MainForm mainForm, Features features) throws HeadlessException {
    super(mainForm);
    this.mainForm = mainForm;
    this.features = features;
  }

  @Override
  protected void initComponents() {

    this.setLayout(new GridLayout(3, 1));

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

    ActionListener actionListener = getActionListenerForCreatePortfolio(
            portfolioTextField,
            dateTextField);

    JPanel buttonJPanel = this.getActionButtonJPanel(actionListener);

    this.addJFrameClosingEvent();

    this.add(portfolioPanel);
    this.add(datePanel);
    this.add(buttonJPanel);
  }

  @Override
  protected void appendOutput(String message) {
    this.mainForm.appendOutput(message);
  }

  private ActionListener getActionListenerForCreatePortfolio(JTextField portfolioTextField,
                                                             JTextField dateTextField) {
    return e -> {
      if (this.isTextFieldEmpty(portfolioTextField)) {
        this.showError("Portfolio Name cannot be empty");
        return;
      }

      if (this.isTextFieldEmpty(dateTextField)) {
        this.showError("Date cannot be empty");
        return;
      }

      String dateString = dateTextField.getText();
      Date date;
      try {
        date = Utils.getDateFromDefaultFormattedDateString(dateString);
      } catch (ParseException e1) {
        this.showError("Invalid Date Format");
        return;
      }

//      this.executeFeature(portfolioTextField.getText(), date);
      //todo insert command here

      this.appendOutput("Get portfolio cost basis");
      this.showPrevious();
    };
  }

  protected void executeFeature(String portfolioName, Date date) {
    this.features.getPortfolioCostBasis(portfolioName, date);
  }
}
