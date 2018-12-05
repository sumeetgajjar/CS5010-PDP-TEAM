package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import javax.swing.*;

import virtualgambling.controller.Features;
import virtualgambling.model.bean.SharePurchaseOrder;

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

      Optional<SharePurchaseOrder> optional = this.features.buyShares(tickerName,
              portfolioName, date, quantity, commission);
      if (optional.isPresent()) {
        SharePurchaseOrder sharePurchaseOrder = optional.get();
        this.mainForm.display(sharePurchaseOrder.toString());
      }

      this.showPrevious();
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
