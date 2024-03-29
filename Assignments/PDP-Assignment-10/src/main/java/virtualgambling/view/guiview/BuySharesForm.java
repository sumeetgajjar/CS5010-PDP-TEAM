package virtualgambling.view.guiview;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import util.Utils;
import virtualgambling.controller.TradingFeatures;
import virtualgambling.model.bean.SharePurchaseOrder;

/**
 * This class represents a GUI form to buys shares. It extends {@link AbstractForm} to reduce the
 * effort in implementing the common functionality.
 */
public class BuySharesForm extends AbstractForm {

  private final MainForm mainForm;
  private final TradingFeatures tradingFeatures;

  /**
   * Constructs a BuySharesForm Object with the given params.
   *
   * @param mainForm        the mainForm
   * @param tradingFeatures the tradingFeatures
   * @throws IllegalArgumentException if the given params are null
   */
  public BuySharesForm(MainForm mainForm, TradingFeatures tradingFeatures)
          throws IllegalArgumentException {
    super(mainForm);
    this.mainForm = Utils.requireNonNull(mainForm);
    this.tradingFeatures = Utils.requireNonNull(tradingFeatures);
    this.setTitle("Buy Shares");
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
    JLabel commissionLabel = new JLabel("Please enter the commission percentage per transaction");
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
      boolean areInputsEmpty = this.areInputsEmpty(tickerNameTextField,
              portfolioTextField,
              dateTextField,
              quantityTextField,
              commissionTextField);

      if (areInputsEmpty) {
        return;
      }

      String tickerName = tickerNameTextField.getText();
      String portfolioName = portfolioTextField.getText();
      Date date = getDateFromTextField(dateTextField, this::showError);
      if (Objects.isNull(date)) {
        return;
      }

      Long quantity = getLongFromTextField(quantityTextField, this::showError);
      if (Objects.isNull(quantity)) {
        return;
      }

      Double commission = getDoubleFromTextField(commissionTextField, this::showError);
      if (Objects.isNull(commission)) {
        return;
      }

      Optional<SharePurchaseOrder> optional = this.tradingFeatures.buyShares(tickerName,
              portfolioName, date, quantity, commission);

      if (optional.isPresent()) {
        this.mainForm.display(optional.get().toString());
        this.showPrevious();
      }
    };
  }

  private boolean areInputsEmpty(JTextField tickerNameTextField,
                                 JTextField portfolioTextField,
                                 JTextField dateTextField,
                                 JTextField quantityTextField,
                                 JTextField commissionTextField) {

    return isTickerNameTextFieldEmpty(tickerNameTextField) ||
            isPortfolioNameTextFieldEmpty(portfolioTextField) ||
            isDateTextFieldEmpty(dateTextField) ||
            isQuantityTextFieldEmpty(quantityTextField) ||
            isCommissionTextFieldEmpty(commissionTextField);
  }

}
