package virtualgambling.view.guiview;

import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 10:48 AM, 12/6/18
 */
public class PersistRecurrentStrategyWithSameWeightsForm extends
        PersistRecurrentStrategyWithDifferentWeightsForm {

  public PersistRecurrentStrategyWithSameWeightsForm(MainForm mainForm, Features features) {
    super(mainForm, features);
    this.stockPercentageJTextField.setVisible(false);
    this.stockPercentageJLabel.setVisible(false);
  }

  @Override
  protected ActionListener getActionListenerForAddStockButton(JTextArea stocksJTextArea,
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
      success = this.features.saveStrategy(this.selectedFile.getAbsolutePath(), startDate,
              dayFrequency,
              this.stockPercentageMap.keySet());
    } else {
      Date endDate = getDateFromTextField(endDateTextField, this.mainForm::displayError);
      if (Objects.isNull(endDate)) {
        return false;
      }
      success = this.features.saveStrategy(this.selectedFile.getAbsolutePath(), startDate,
              endDate,
              dayFrequency,
              this.stockPercentageMap.keySet());
    }
    return success;
  }
}
