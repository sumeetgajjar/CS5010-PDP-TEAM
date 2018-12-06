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

    JButton oneTimeDifferentWeights = new JButton("One Time Strategy");
    oneTimeDifferentWeights.addActionListener(e -> {
      OneTimeStrategyForm oneTimeStrategyForm =
              new OneTimeStrategyForm(this, features);
      GUIUtils.showPrevious(oneTimeStrategyForm, this);
    });
    this.add(oneTimeDifferentWeights);

    JButton recurringDifferentWeights = new JButton("Recurring Strategy");
    recurringDifferentWeights.addActionListener(e -> {
      RecurrentStrategyForm recurrentStrategyForm =
              new RecurrentStrategyForm(this, features);
      GUIUtils.showPrevious(recurrentStrategyForm, this);
    });
    this.add(recurringDifferentWeights);
  }
}
