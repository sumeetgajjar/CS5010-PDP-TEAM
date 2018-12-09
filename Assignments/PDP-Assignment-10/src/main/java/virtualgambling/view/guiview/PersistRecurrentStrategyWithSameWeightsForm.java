package virtualgambling.view.guiview;

import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import virtualgambling.controller.TradingFeatures;

/**
 * This class represents a GUI form to Persists a Strategy with Same weights. It extends {@link
 * PersistRecurrentStrategyWithDifferentWeightsForm} to reduce the effort in implementing the common
 * functionality.
 */
public class PersistRecurrentStrategyWithSameWeightsForm extends
        PersistRecurrentStrategyWithDifferentWeightsForm {

  /**
   * Constructs a object of PersistRecurrentStrategyWithSameWeightsForm with the given params.
   *
   * @param mainForm the mainForm
   * @param tradingFeatures the tradingFeatures
   * @throws IllegalArgumentException if the given params are null
   */
  public PersistRecurrentStrategyWithSameWeightsForm(MainForm mainForm,
                                                     TradingFeatures tradingFeatures)
          throws IllegalArgumentException {

    super(mainForm, tradingFeatures);
    this.stockPercentageJTextField.setVisible(false);
    this.stockPercentageJLabel.setVisible(false);
  }

  @Override
  protected ActionListener getActionListenerForAddStockButton(
          JTextArea stocksJTextArea,
          JTextField stockNameJTextField,
          JTextField stockPercentageJTextField) {
    return getActionListenerForAddStockButtonForSameWeight(stocksJTextArea, stockNameJTextField,
            this.stockPercentageMap);
  }

  @Override
  protected boolean executeFeature(Date startDate,
                                   JTextField endDateTextField,
                                   int dayFrequency) {

    boolean success;
    if (endDateTextField.getText().isEmpty()) {
      success = this.tradingFeatures.saveStrategy(this.selectedFile.getAbsolutePath(), startDate,
              dayFrequency,
              this.stockPercentageMap.keySet());
    } else {
      Date endDate = getDateFromTextField(endDateTextField, this.mainForm::displayError);
      if (Objects.isNull(endDate)) {
        return false;
      }
      success = this.tradingFeatures.saveStrategy(this.selectedFile.getAbsolutePath(), startDate,
              endDate,
              dayFrequency,
              this.stockPercentageMap.keySet());
    }
    return success;
  }
}
