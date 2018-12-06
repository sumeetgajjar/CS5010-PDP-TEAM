package virtualgambling.view.guiview;

import java.awt.*;

import javax.swing.*;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 1:52 AM, 12/6/18
 */
public class SelectStrategyForm extends AbstractForm {

  private final MainForm mainForm;
  private final Features features;

  public SelectStrategyForm(MainForm mainForm, Features features) {
    super(mainForm);
    this.mainForm = mainForm;
    this.features = features;
  }

  @Override
  protected void initComponents() {
    this.setLayout(new GridLayout(4, 1));

    JButton oneTimeDifferentWeights = new JButton("One Time Strategy with Different Weights");
    oneTimeDifferentWeights.addActionListener(e -> {
      OneTimeStrategyWithDifferentWeightsForm oneTimeStrategyWithDifferentWeightsForm =
              new OneTimeStrategyWithDifferentWeightsForm(mainForm, features);
      GUIUtils.showPrevious(oneTimeStrategyWithDifferentWeightsForm, this);
    });
    this.add(oneTimeDifferentWeights);

    JButton oneTimeSameWeights = new JButton("One Time Strategy with Same Weights");
    oneTimeSameWeights.addActionListener(e -> {
      OneTimeStrategyWithSameWeightsForm oneTimeStrategyWithSameWeightsForm =
              new OneTimeStrategyWithSameWeightsForm(mainForm, features);
      GUIUtils.showPrevious(oneTimeStrategyWithSameWeightsForm, this);
    });
    this.add(oneTimeSameWeights);

    JButton recurringDifferentWeights = new JButton("Recurring Strategy with Different Weights");
    recurringDifferentWeights.addActionListener(e -> {
      RecurrentStrategyWithDifferentWeightsForm recurrentStrategyWithDifferentWeightsForm =
              new RecurrentStrategyWithDifferentWeightsForm(mainForm, features);
      GUIUtils.showPrevious(recurrentStrategyWithDifferentWeightsForm, this);
    });
    this.add(recurringDifferentWeights);

    JButton recurringSameWeights = new JButton("Recurring Strategy with Same Weights");
    recurringSameWeights.addActionListener(e -> {
      RecurrentStrategyWithSameWeightsForm recurrentStrategyWithSameWeightsForm =
              new RecurrentStrategyWithSameWeightsForm(mainForm, features);
      GUIUtils.showPrevious(recurrentStrategyWithSameWeightsForm, this);
    });
    this.add(recurringSameWeights);
  }
}
