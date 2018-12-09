package virtualgambling.view.guiview;

import java.awt.GridLayout;

import javax.swing.JButton;

import util.Utils;
import virtualgambling.controller.TradingFeatures;

/**
 * This class represents a GUI form to select a strategy to buy shares. It extends {@link
 * AbstractForm} to reduce the effort in implementing the common functionality.
 */
public class SelectStrategyToBuySharesForm extends AbstractForm {

  private final MainForm mainForm;
  private final TradingFeatures tradingFeatures;

  /**
   * Constructs a object of SelectStrategyToBuySharesForm with the given params.
   *
   * @param mainForm the mainForm
   * @param tradingFeatures the tradingFeatures
   * @throws IllegalArgumentException if the given params are null
   */
  public SelectStrategyToBuySharesForm(MainForm mainForm, TradingFeatures tradingFeatures)
          throws IllegalArgumentException {
    super(mainForm);
    this.mainForm = Utils.requireNonNull(mainForm);
    this.tradingFeatures = Utils.requireNonNull(tradingFeatures);
    this.setTitle("Buy Shares using Strategy");
  }

  @Override
  protected void initComponents() {
    this.setLayout(new GridLayout(4, 1));

    JButton oneTimeDifferentWeights = new JButton("One Time Strategy with Different Weights");
    oneTimeDifferentWeights.addActionListener(e -> {
      BuySharesUsingOneTimeStrategyWithDifferentWeightsForm
              buySharesUsingOneTimeStrategyWithDifferentWeightsForm =
              new BuySharesUsingOneTimeStrategyWithDifferentWeightsForm(mainForm, tradingFeatures);
      Utils.showPrevious(buySharesUsingOneTimeStrategyWithDifferentWeightsForm, this);
    });
    this.add(oneTimeDifferentWeights);

    JButton oneTimeSameWeights = new JButton("One Time Strategy with Same Weights");
    oneTimeSameWeights.addActionListener(e -> {
      BuySharesUsingOneTimeStrategyWithSameWeightsForm oneTimeStrategyWithSameWeightsForm =
              new BuySharesUsingOneTimeStrategyWithSameWeightsForm(mainForm, tradingFeatures);
      Utils.showPrevious(oneTimeStrategyWithSameWeightsForm, this);
    });
    this.add(oneTimeSameWeights);

    JButton recurringDifferentWeights = new JButton("Recurring Strategy with Different Weights");
    recurringDifferentWeights.addActionListener(e -> {
      BuySharesUsingRecurrentStrategyWithDifferentWeightsForm
              buySharesUsingRecurrentStrategyWithDifferentWeightsForm =
              new BuySharesUsingRecurrentStrategyWithDifferentWeightsForm(mainForm,
                      tradingFeatures);
      Utils.showPrevious(buySharesUsingRecurrentStrategyWithDifferentWeightsForm, this);
    });
    this.add(recurringDifferentWeights);

    JButton recurringSameWeights = new JButton("Recurring Strategy with Same Weights");
    recurringSameWeights.addActionListener(e -> {
      BuySharesUsingRecurrentStrategyWithSameWeightsForm recurrentStrategyWithSameWeightsForm =
              new BuySharesUsingRecurrentStrategyWithSameWeightsForm(mainForm, tradingFeatures);
      Utils.showPrevious(recurrentStrategyWithSameWeightsForm, this);
    });
    this.add(recurringSameWeights);
  }
}
