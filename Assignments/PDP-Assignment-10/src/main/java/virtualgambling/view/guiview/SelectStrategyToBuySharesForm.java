package virtualgambling.view.guiview;

import java.awt.*;

import javax.swing.*;

import util.Utils;
import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 1:52 AM, 12/6/18
 */
public class SelectStrategyToBuySharesForm extends AbstractForm {

  private final MainForm mainForm;
  private final Features features;

  public SelectStrategyToBuySharesForm(MainForm mainForm, Features features) {
    super(mainForm);
    this.mainForm = mainForm;
    this.features = features;
    this.setTitle("Buy Shares using Strategy");
  }

  @Override
  protected void initComponents() {
    this.setLayout(new GridLayout(4, 1));

    JButton oneTimeDifferentWeights = new JButton("One Time Strategy with Different Weights");
    oneTimeDifferentWeights.addActionListener(e -> {
      BuySharesUsingOneTimeStrategyWithDifferentWeightsForm buySharesUsingOneTimeStrategyWithDifferentWeightsForm =
              new BuySharesUsingOneTimeStrategyWithDifferentWeightsForm(mainForm, features);
      Utils.showPrevious(buySharesUsingOneTimeStrategyWithDifferentWeightsForm, this);
    });
    this.add(oneTimeDifferentWeights);

    JButton oneTimeSameWeights = new JButton("One Time Strategy with Same Weights");
    oneTimeSameWeights.addActionListener(e -> {
      BuySharesUsingOneTimeStrategyWithSameWeightsForm oneTimeStrategyWithSameWeightsForm =
              new BuySharesUsingOneTimeStrategyWithSameWeightsForm(mainForm, features);
      Utils.showPrevious(oneTimeStrategyWithSameWeightsForm, this);
    });
    this.add(oneTimeSameWeights);

    JButton recurringDifferentWeights = new JButton("Recurring Strategy with Different Weights");
    recurringDifferentWeights.addActionListener(e -> {
      BuySharesUsingRecurrentStrategyWithDifferentWeightsForm buySharesUsingRecurrentStrategyWithDifferentWeightsForm =
              new BuySharesUsingRecurrentStrategyWithDifferentWeightsForm(mainForm, features);
      Utils.showPrevious(buySharesUsingRecurrentStrategyWithDifferentWeightsForm, this);
    });
    this.add(recurringDifferentWeights);

    JButton recurringSameWeights = new JButton("Recurring Strategy with Same Weights");
    recurringSameWeights.addActionListener(e -> {
      BuySharesUsingRecurrentStrategyWithSameWeightsForm recurrentStrategyWithSameWeightsForm =
              new BuySharesUsingRecurrentStrategyWithSameWeightsForm(mainForm, features);
      Utils.showPrevious(recurrentStrategyWithSameWeightsForm, this);
    });
    this.add(recurringSameWeights);
  }
}
