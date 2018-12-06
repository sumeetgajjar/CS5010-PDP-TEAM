package virtualgambling.view.guiview;

import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.swing.*;

import virtualgambling.controller.Features;
import virtualgambling.model.bean.SharePurchaseOrder;

/**
 * Created by gajjar.s, on 3:44 AM, 12/6/18
 */
public class RecurrentStrategyWithSameWeightsForm extends RecurrentStrategyWithDifferentWeightsForm {

  public RecurrentStrategyWithSameWeightsForm(MainForm mainForm, Features features) {
    super(mainForm, features);
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
