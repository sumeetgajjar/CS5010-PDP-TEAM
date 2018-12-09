package virtualgambling.view.guiview;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import util.Utils;
import virtualgambling.controller.TradingFeatures;
import virtualgambling.model.bean.SharePurchaseOrder;

/**
 * This class represents a GUI form to buys shares of different weights using the recurrent
 * strategy. It extends {@link AbstractForm} to reduce the effort in implementing the common
 * functionality.
 */
public class BuySharesUsingRecurrentStrategyWithDifferentWeightsForm extends AbstractForm {

  protected final MainForm mainForm;
  protected final TradingFeatures tradingFeatures;
  protected final Map<String, Double> stockPercentageMap;
  protected JTextField stockPercentageJTextField;
  protected JLabel stockPercentageJLabel;
  private JButton addStockJButton;
  private JTextArea stocksJTextArea;
  private JTextField stockNameJTextField;

  /**
   * Constructs a object of BuySharesUsingRecurrentStrategyWithDifferentWeightsForm with the given
   * params.
   *
   * @param mainForm the mainForm
   * @param tradingFeatures the tradingFeatures
   * @throws IllegalArgumentException if the given params are null
   */
  public BuySharesUsingRecurrentStrategyWithDifferentWeightsForm(MainForm mainForm,
                                                                 TradingFeatures tradingFeatures)
          throws IllegalArgumentException {

    super(mainForm);
    this.mainForm = Utils.requireNonNull(mainForm);
    this.tradingFeatures = Utils.requireNonNull(tradingFeatures);
    this.stockPercentageMap = new LinkedHashMap<>();
    this.addActionListenerToAddStockButton();
    this.setTitle("Buy Shares");
  }

  @Override
  protected void initComponents() {
    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

    JPanel jPanel1 = new JPanel();
    jPanel1.add(new JLabel("Leave End date field blank if there is no end date"));
    container.add(jPanel1);

    JPanel jPanel = new JPanel();
    jPanel.setLayout(new GridLayout(6, 2));

    JLabel jLabel = new JLabel("Portfolio Name:");
    jPanel.add(jLabel);
    JTextField portfolioNameJTextField = new JTextField(10);
    jPanel.add(portfolioNameJTextField);

    JLabel startDateLabel = new JLabel("Start date (YYYY-MM-DD):");
    jPanel.add(startDateLabel);
    JTextField startDateTextField = new JTextField(10);
    jPanel.add(startDateTextField);

    JLabel endDateLabel = new JLabel("End date (YYYY-MM-DD):");
    jPanel.add(endDateLabel);
    JTextField endDateTextField = new JTextField(10);
    jPanel.add(endDateTextField);

    JLabel recurringIntervalLabel = new JLabel("Recurring Interval (Days):");
    jPanel.add(recurringIntervalLabel);
    JTextField recurringIntervalDaysTextField = new JTextField(10);
    jPanel.add(recurringIntervalDaysTextField);

    JLabel amountToInvestLabel = new JLabel("Amount To Invest (Dollars):");
    jPanel.add(amountToInvestLabel);
    JTextField amountToInvestTextField = new JTextField(10);
    jPanel.add(amountToInvestTextField);

    JLabel commissionLabel = new JLabel("Commission Percentage:");
    jPanel.add(commissionLabel);
    JTextField commissionPercentageJTextField = new JTextField(10);
    jPanel.add(commissionPercentageJTextField);

    container.add(jPanel);

    stocksJTextArea = new JTextArea();
    stocksJTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
    stocksJTextArea.setEditable(false);

    JPanel stocksAdditionPanel = new JPanel();
    stocksAdditionPanel.add(new JLabel("Ticker Name:"));
    stockNameJTextField = new JTextField(10);
    stocksAdditionPanel.add(stockNameJTextField);

    stockPercentageJLabel = new JLabel("Stock Percentage:");
    stocksAdditionPanel.add(stockPercentageJLabel);
    stockPercentageJTextField = new JTextField(10);
    stocksAdditionPanel.add(stockPercentageJTextField);

    addStockJButton = new JButton("Add Stock");

    stocksAdditionPanel.add(addStockJButton);

    container.add(stocksAdditionPanel);

    JScrollPane outputJPanel = new JScrollPane(stocksJTextArea);
    outputJPanel.setPreferredSize(new Dimension(400, 200));

    container.add(outputJPanel);
    container.add(this.getActionButtonJPanel(
            getActionListenerForExecuteButton(
                    portfolioNameJTextField,
                    startDateTextField,
                    endDateTextField,
                    recurringIntervalDaysTextField,
                    amountToInvestTextField,
                    commissionPercentageJTextField)));

    this.add(container);
  }

  protected Optional<List<SharePurchaseOrder>> executeFeature(String portfolioName,
                                                              Date startDate,
                                                              Integer dayFrequency,
                                                              BigDecimal amountToInvest,
                                                              Double commissionPercentage,
                                                              JTextField endDateTextField) {
    if (endDateTextField.getText().isEmpty()) {
      return this.tradingFeatures.buyShares(portfolioName, startDate, dayFrequency,
              this.stockPercentageMap,
              amountToInvest, commissionPercentage);
    } else {
      Date endDate = getDateFromTextField(endDateTextField, this.mainForm::displayError);
      if (Objects.isNull(endDate)) {
        return Optional.empty();
      }
      return this.tradingFeatures.buyShares(portfolioName, startDate, endDate,
              dayFrequency,
              this.stockPercentageMap, amountToInvest, commissionPercentage);
    }
  }

  protected ActionListener getActionListenerForAddStockButton(
          JTextArea stocksJTextArea,
          JTextField stockNameJTextField,
          JTextField stockPercentageJTextField) {
    return getActionListenerForAddStockButtonForDifferentWeight(
            stocksJTextArea,
            stockNameJTextField,
            stockPercentageJTextField,
            this.stockPercentageMap,
            this.mainForm::displayError);
  }

  private void addActionListenerToAddStockButton() {
    addStockJButton.addActionListener(
            getActionListenerForAddStockButton(
                    stocksJTextArea,
                    stockNameJTextField,
                    stockPercentageJTextField));
  }

  private ActionListener getActionListenerForExecuteButton(
          JTextField portfolioNameJTextField,
          JTextField startDateTextField,
          JTextField endDateTextField,
          JTextField recurringIntervalDaysTextField,
          JTextField amountToInvestTextField,
          JTextField commissionPercentageJTextField) {
    return e -> {
      if (this.areInputsEmpty(portfolioNameJTextField, startDateTextField,
              recurringIntervalDaysTextField, amountToInvestTextField,
              commissionPercentageJTextField)) {
        return;
      }

      String portfolioName = portfolioNameJTextField.getText();
      Date startDate = getDateFromTextField(startDateTextField, this.mainForm::displayError);
      if (Objects.isNull(startDate)) {
        return;
      }

      Integer dayFrequency = getIntegerFromTextField(recurringIntervalDaysTextField,
              this.mainForm::displayError);
      if (Objects.isNull(dayFrequency)) {
        return;
      }

      BigDecimal amountToInvest = getBigDecimalFromTextField(amountToInvestTextField,
              this.mainForm::displayError);
      if (Objects.isNull(amountToInvest)) {
        return;
      }

      Double commissionPercentage = getDoubleFromTextField(commissionPercentageJTextField,
              this.mainForm::displayError);
      if (Objects.isNull(commissionPercentage)) {
        return;
      }

      Optional<List<SharePurchaseOrder>> sharePurchaseOrders = executeFeature(
              portfolioName, startDate, dayFrequency, amountToInvest, commissionPercentage,
              endDateTextField);

      this.displaySharePurchaseOrder(sharePurchaseOrders, this.mainForm::display);
    };
  }

  private boolean areInputsEmpty(JTextField portfolioNameJTextField,
                                 JTextField startDateTextField,
                                 JTextField recurringIntervalDaysTextField,
                                 JTextField amountToInvestTextField,
                                 JTextField commissionPercentageJTextField) {

    return isPortfolioNameTextFieldEmpty(portfolioNameJTextField) ||
            isStartDateTextFieldEmpty(startDateTextField) ||
            isRecurringIntervalTextFieldEmpty(recurringIntervalDaysTextField) ||
            isAmountToInvestTextFieldEmpty(amountToInvestTextField) ||
            isCommissionTextFieldEmpty(commissionPercentageJTextField);
  }
}
