package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

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
      if (this.areInputsEmpty(portfolioTextField, dateTextField)) {
        return;
      }

      Date date = getDateFromTextField(dateTextField, this::showError);
      if (Objects.isNull(date)) {
        return;
      }

      String portfolioName = portfolioTextField.getText();
      this.executeFeature(portfolioName, date);

      this.showPrevious();
    };
  }

  private boolean areInputsEmpty(JTextField portfolioTextField, JTextField dateTextField) {
    return isPortfolioNameTextFieldEmpty(portfolioTextField) ||
            isDateTextFieldEmpty(dateTextField);
  }

  protected void executeFeature(String portfolioName, Date date) {
    Optional<BigDecimal> portfolioCostBasis = this.features.getPortfolioCostBasis(portfolioName,
            date);
    portfolioCostBasis.ifPresent(bigDecimal -> {
      String numberString = Utils.getFormattedCurrencyNumberString(bigDecimal);
      this.mainForm.display(numberString);
    });
  }
}
