package virtualgambling.view.guiview;

import java.awt.GridLayout;

import javax.swing.JButton;

import util.Utils;
import virtualgambling.controller.TradingFeatures;

/**
 * This class represents a GUI form to create strategy and persists it to a file. It extends {@link
 * AbstractForm} to reduce the effort in implementing the common functionality.
 */
public class CreateStrategyAndPersistForm extends AbstractForm {

  private final MainForm mainForm;
  private final TradingFeatures tradingFeatures;

  /**
   * Constructs a object of CreateStrategyAndPersistForm with the given params.
   *
   * @param mainForm the mainForm
   * @param tradingFeatures the tradingFeatures
   * @throws IllegalArgumentException if the given params are null
   */
  public CreateStrategyAndPersistForm(MainForm mainForm, TradingFeatures tradingFeatures)
          throws IllegalArgumentException {
    super(mainForm);
    this.mainForm = Utils.requireNonNull(mainForm);
    this.tradingFeatures = Utils.requireNonNull(tradingFeatures);
    this.setTitle("Create and Persist Strategy");
  }

  @Override
  protected void initComponents() {
    this.setLayout(new GridLayout(2, 1));

    JButton recurringDifferentWeights = new JButton("Recurring Strategy with Different Weights");
    recurringDifferentWeights.addActionListener(e -> {
      PersistRecurrentStrategyWithDifferentWeightsForm
              persistRecurrentStrategyWithDifferentWeightsForm =
              new PersistRecurrentStrategyWithDifferentWeightsForm(mainForm, tradingFeatures);
      Utils.showPrevious(persistRecurrentStrategyWithDifferentWeightsForm, this);
    });
    this.add(recurringDifferentWeights);

    JButton recurringSameWeights = new JButton("Recurring Strategy with Same Weights");
    recurringSameWeights.addActionListener(e -> {
      PersistRecurrentStrategyWithSameWeightsForm persistRecurrentStrategyWithSameWeightsForm =
              new PersistRecurrentStrategyWithSameWeightsForm(mainForm, tradingFeatures);
      Utils.showPrevious(persistRecurrentStrategyWithSameWeightsForm, this);
    });
    this.add(recurringSameWeights);
  }
}
