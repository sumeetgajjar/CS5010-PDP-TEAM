package virtualgambling.view.guiview;

import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import virtualgambling.controller.TradingFeatures;
import virtualgambling.model.bean.SharePurchaseOrder;

/**
 * This class represents a GUI form to buys shares of Equal weights using the one time strategy. It
 * extends {@link BuySharesUsingOneTimeStrategyWithDifferentWeightsForm} to reduce the effort in
 * implementing the common functionality.
 */
public class BuySharesUsingOneTimeStrategyWithSameWeightsForm
        extends BuySharesUsingOneTimeStrategyWithDifferentWeightsForm {

  /**
   * Constructs a object of BuySharesUsingOneTimeStrategyWithSameWeightsForm with the given params.
   *
   * @param mainForm        the mainForm
   * @param tradingFeatures the tradingFeatures
   * @throws IllegalArgumentException if the given params are null
   */
  public BuySharesUsingOneTimeStrategyWithSameWeightsForm(MainForm mainForm,
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
  protected Optional<List<SharePurchaseOrder>> executeFeature(String portfolioName,
                                                              Date startDate,
                                                              BigDecimal amountToInvest,
                                                              Double commissionPercentage) {
    return this.tradingFeatures.buyShares(portfolioName, startDate,
            this.stockPercentageMap.keySet(),
            amountToInvest, commissionPercentage);

  }
}
