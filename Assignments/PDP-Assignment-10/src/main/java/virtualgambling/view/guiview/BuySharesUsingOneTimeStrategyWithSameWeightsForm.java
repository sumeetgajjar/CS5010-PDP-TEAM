package virtualgambling.view.guiview;

import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import virtualgambling.controller.Features;
import virtualgambling.model.bean.SharePurchaseOrder;

/**
 * Created by gajjar.s, on 4:40 AM, 12/6/18
 */
public class BuySharesUsingOneTimeStrategyWithSameWeightsForm extends BuySharesUsingOneTimeStrategyWithDifferentWeightsForm {

  public BuySharesUsingOneTimeStrategyWithSameWeightsForm(MainForm mainForm, Features features) {
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
  protected Optional<List<SharePurchaseOrder>> executeFeature(String portfolioName,
                                                              Date startDate,
                                                              BigDecimal amountToInvest,
                                                              Double commissionPercentage) {
    return this.features.buyShares(portfolioName, startDate, this.stockPercentageMap.keySet(),
            amountToInvest, commissionPercentage);

  }
}
