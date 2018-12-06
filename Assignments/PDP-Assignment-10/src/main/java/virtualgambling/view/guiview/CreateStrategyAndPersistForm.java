package virtualgambling.view.guiview;

import java.awt.*;

import javax.swing.*;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 1:52 AM, 12/6/18
 */
public class CreateStrategyAndPersistForm extends AbstractForm {

  private final MainForm mainForm;
  private final Features features;

  public CreateStrategyAndPersistForm(MainForm mainForm, Features features) {
    super(mainForm);
    this.mainForm = mainForm;
    this.features = features;
  }

  @Override
  protected void initComponents() {
    this.setLayout(new GridLayout(2, 1));

    JButton recurringDifferentWeights = new JButton("Recurring Strategy with Different Weights");
    recurringDifferentWeights.addActionListener(e -> {
      PersistRecurrentStrategyWithDifferentWeightsForm persistRecurrentStrategyWithDifferentWeightsForm =
              new PersistRecurrentStrategyWithDifferentWeightsForm(mainForm, features);
      GUIUtils.showPrevious(persistRecurrentStrategyWithDifferentWeightsForm, this);
    });
    this.add(recurringDifferentWeights);
  }
}
