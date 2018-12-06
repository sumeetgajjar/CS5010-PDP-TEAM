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

    JButton oneTimeDifferentWeights = new JButton("One Time with Different Stock weights");
    this.add(oneTimeDifferentWeights);

    JButton oneTimeSameWeights = new JButton("One Time with Same Stock weights");
    this.add(oneTimeSameWeights);

    JButton recurringDifferentWeights = new JButton("Recurring with Different Stock weights");
    this.add(recurringDifferentWeights);

    JButton recurringSameWeights = new JButton("Recurring with Same Stock weights");
    this.add(recurringSameWeights);

  }
}
