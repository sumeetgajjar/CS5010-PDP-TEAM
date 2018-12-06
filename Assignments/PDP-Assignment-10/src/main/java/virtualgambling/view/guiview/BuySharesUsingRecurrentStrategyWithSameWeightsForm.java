package virtualgambling.view.guiview;

import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import virtualgambling.controller.Features;
import virtualgambling.model.bean.SharePurchaseOrder;

/**
 * This class represents a GUI form to buys shares of same weights using the recurrent strategy. It
 * extends {@link BuySharesUsingRecurrentStrategyWithDifferentWeightsForm} to reduce the effort in
 * implementing the common functionality.
 */
public class BuySharesUsingRecurrentStrategyWithSameWeightsForm
        extends BuySharesUsingRecurrentStrategyWithDifferentWeightsForm {

  /**
   * Constructs a object of BuySharesUsingRecurrentStrategyWithSameWeightsForm with the given
   * params.
   *
   * @param mainForm the mainForm
   * @param features the features
   * @throws IllegalArgumentException if the given params are null
   */
  public BuySharesUsingRecurrentStrategyWithSameWeightsForm(MainForm mainForm, Features features)
          throws IllegalArgumentException {

    super(mainForm, features);
    this.stockPercentageJTextField.setVisible(false);
    this.stockPercentageJLabel.setVisible(false);
  }

  @Override
  protected ActionListener getActionListenerForAddStockButton(JTextArea stocksJTextArea,
                                                              JTextField stockNameJTextField,
                                                              JTextField stockPercentageJTextField) {
    return e -> {
      if (this.isTickerNameTextFieldEmpty(stockNameJTextField)) {
        return;
      }

      String tickerName = stockNameJTextField.getText();

      this.stockPercentageMap.put(tickerName, 0D);

      StringBuilder builder = new StringBuilder();
      for (Map.Entry<String, Double> entry : this.stockPercentageMap.entrySet()) {
        builder.append(entry.getKey());
        builder.append(System.lineSeparator());
      }

      stocksJTextArea.setText(builder.toString());
    };
  }

  @Override
  protected Optional<List<SharePurchaseOrder>> executeFeature(String portfolioName,
                                                              Date startDate,
                                                              Integer dayFrequency,
                                                              BigDecimal amountToInvest,
                                                              Double commissionPercentage,
                                                              JTextField endDateTextField) {
    if (endDateTextField.getText().isEmpty()) {
      return this.features.buyShares(portfolioName, startDate, dayFrequency,
              this.stockPercentageMap.keySet(),
              amountToInvest, commissionPercentage);
    } else {
      Date endDate = getDateFromTextField(endDateTextField, this.mainForm::displayError);
      if (Objects.isNull(endDate)) {
        return Optional.empty();
      }
      return this.features.buyShares(portfolioName, startDate, endDate,
              dayFrequency,
              this.stockPercentageMap.keySet(), amountToInvest, commissionPercentage);
    }
  }
}
