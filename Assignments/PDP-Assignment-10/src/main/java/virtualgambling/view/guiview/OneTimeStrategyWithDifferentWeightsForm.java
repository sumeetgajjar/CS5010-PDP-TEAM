package virtualgambling.view.guiview;

import java.awt.*;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.swing.*;

import virtualgambling.controller.Features;
import virtualgambling.model.bean.SharePurchaseOrder;

/**
 * Created by gajjar.s, on 2:19 AM, 12/6/18
 */
public class OneTimeStrategyWithDifferentWeightsForm extends AbstractForm {

  protected final MainForm mainForm;
  protected final Features features;
  protected final Map<String, Double> stockPercentageMap;
  protected JTextField stockPercentageJTextField;
  protected JLabel stockPercentageJLabel;

  public OneTimeStrategyWithDifferentWeightsForm(MainForm mainForm, Features features) {
    super(mainForm);
    this.mainForm = mainForm;
    this.features = features;
    this.stockPercentageMap = new LinkedHashMap<>();
  }

  @Override
  protected void initComponents() {
    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

    JPanel jPanel1 = new JPanel();
    jPanel1.add(new JLabel("Leave End date field blank if there is no end date"));
    container.add(jPanel1);

    JPanel jPanel = new JPanel();
    jPanel.setLayout(new GridLayout(4, 2));

    JLabel jLabel = new JLabel("Portfolio Name:");
    jPanel.add(jLabel);
    JTextField portfolioNameJTextField = new JTextField(10);
    jPanel.add(portfolioNameJTextField);

    JLabel startDateLabel = new JLabel("Investment date (YYYY-MM-DD):");
    jPanel.add(startDateLabel);
    JTextField startDateTextField = new JTextField(10);
    jPanel.add(startDateTextField);

    JLabel amountToInvestLabel = new JLabel("Amount To Invest (Dollars):");
    jPanel.add(amountToInvestLabel);
    JTextField amountToInvestTextField = new JTextField(10);
    jPanel.add(amountToInvestTextField);

    JLabel commissionLabel = new JLabel("Commission Percentage:");
    jPanel.add(commissionLabel);
    JTextField commissionPercentageJTextField = new JTextField(10);
    jPanel.add(commissionPercentageJTextField);

    container.add(jPanel);

    JTextArea stocksJTextArea = new JTextArea();
    stocksJTextArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
    stocksJTextArea.setEditable(false);

    JPanel stocksAdditionPanel = new JPanel();
    stocksAdditionPanel.add(new JLabel("Ticker Name:"));
    JTextField stockNameJTextField = new JTextField(10);
    stocksAdditionPanel.add(stockNameJTextField);

    stockPercentageJLabel = new JLabel("Stock Percentage:");
    stocksAdditionPanel.add(stockPercentageJLabel);
    stockPercentageJTextField = new JTextField(10);
    stocksAdditionPanel.add(stockPercentageJTextField);

    JButton addStockJButton = new JButton("Add Stock");
    addStockJButton.addActionListener(
            getActionListenerForAddStockButton(
                    stocksJTextArea,
                    stockNameJTextField,
                    stockPercentageJTextField));

    stocksAdditionPanel.add(addStockJButton);

    container.add(stocksAdditionPanel);

    JScrollPane outputJPanel = new JScrollPane(stocksJTextArea);
    outputJPanel.setPreferredSize(new Dimension(400, 200));

    container.add(outputJPanel);
    container.add(this.getActionButtonJPanel(
            getActionListenerForExecuteButton(
                    portfolioNameJTextField,
                    startDateTextField,
                    amountToInvestTextField,
                    commissionPercentageJTextField)));

    this.add(container);
  }

  protected ActionListener getActionListenerForAddStockButton(JTextArea stocksJTextArea,
                                                              JTextField stockNameJTextField,
                                                              JTextField stockPercentageJTextField) {
    return getActionListenerForAddStockButtonForDifferentWeight(
            stocksJTextArea,
            stockNameJTextField,
            stockPercentageJTextField,
            this.stockPercentageMap,
            this.mainForm::displayError);
  }

  private ActionListener
  getActionListenerForExecuteButton(JTextField portfolioNameJTextField,
                                    JTextField startDateTextField,
                                    JTextField amountToInvestTextField,
                                    JTextField commissionPercentageJTextField) {
    return e -> {
      if (this.areInputsEmpty(portfolioNameJTextField, startDateTextField,
              amountToInvestTextField,
              commissionPercentageJTextField)) {
        return;
      }

      String portfolioName = portfolioNameJTextField.getText();
      Date startDate = getDateFromTextField(startDateTextField, this.mainForm::displayError);
      if (Objects.isNull(startDate)) {
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
              portfolioName, startDate, amountToInvest, commissionPercentage);

      this.displaySharePurchaseOrder(sharePurchaseOrders, this.mainForm::display);
    };
  }

  protected Optional<List<SharePurchaseOrder>> executeFeature(String portfolioName,
                                                              Date startDate,
                                                              BigDecimal amountToInvest,
                                                              Double commissionPercentage) {
    return this.features.buyShares(portfolioName, startDate, this.stockPercentageMap,
            amountToInvest, commissionPercentage);
  }

  private boolean areInputsEmpty(JTextField portfolioNameJTextField,
                                 JTextField startDateTextField,
                                 JTextField amountToInvestTextField,
                                 JTextField commissionPercentageJTextField) {

    return isPortfolioNameTextFieldEmpty(portfolioNameJTextField) ||
            isStartDateTextFieldEmpty(startDateTextField) ||
            isAmountToInvestTextFieldEmpty(amountToInvestTextField) ||
            isCommissionTextFieldEmpty(commissionPercentageJTextField);
  }
}
