package virtualgambling.view.guiview;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.Utils;
import virtualgambling.controller.Features;

/**
 * This class represents a GUI form to get the cost basis of a portfolio. It extends {@link
 * AbstractForm} to reduce the effort in implementing the common functionality.
 */
public class GetPortfolioCostBasisForm extends AbstractForm {

  private final MainForm mainForm;
  protected final Features features;

  /**
   * Constructs a object of GetPortfolioCostBasisForm with the given params.
   *
   * @param mainForm the mainForm
   * @param features the features
   * @throws IllegalArgumentException if the given params are null
   */
  public GetPortfolioCostBasisForm(MainForm mainForm, Features features)
          throws IllegalArgumentException {
    super(mainForm);
    this.mainForm = Utils.requireNonNull(mainForm);
    this.features = Utils.requireNonNull(features);
    this.setTitle("Portfolio Cost basis");
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
      Optional<BigDecimal> optional = this.executeFeature(portfolioName, date);
      if (optional.isPresent()) {
        String numberString = Utils.getFormattedCurrencyNumberString(optional.get());
        this.mainForm.display(String.format("%s%s%s", getPrefix(portfolioName, date),
                System.lineSeparator(), numberString));
        this.showPrevious();
      }
    };
  }

  protected String getPrefix(String portfolioName, Date date) {
    return "Cost basis of portfolio '" + portfolioName + "' on '"
            + Utils.getDefaultFormattedDateStringFromDate(date) + "'";
  }

  protected Optional<BigDecimal> executeFeature(String portfolioName, Date date) {
    return this.features.getPortfolioCostBasis(portfolioName, date);
  }

  private boolean areInputsEmpty(JTextField portfolioTextField, JTextField dateTextField) {
    return isPortfolioNameTextFieldEmpty(portfolioTextField) ||
            isDateTextFieldEmpty(dateTextField);
  }
}
