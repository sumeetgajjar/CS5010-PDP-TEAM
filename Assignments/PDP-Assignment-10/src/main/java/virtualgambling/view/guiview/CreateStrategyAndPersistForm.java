package virtualgambling.view.guiview;

import java.awt.GridLayout;

import javax.swing.JButton;

import util.Utils;
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
    this.setTitle("Create and Persist Strategy");
  }

  @Override
  protected void initComponents() {
    this.setLayout(new GridLayout(2, 1));

    JButton recurringDifferentWeights = new JButton("Recurring Strategy with Different Weights");
    recurringDifferentWeights.addActionListener(e -> {
      PersistRecurrentStrategyWithDifferentWeightsForm persistRecurrentStrategyWithDifferentWeightsForm =
              new PersistRecurrentStrategyWithDifferentWeightsForm(mainForm, features);
      Utils.showPrevious(persistRecurrentStrategyWithDifferentWeightsForm, this);
    });
    this.add(recurringDifferentWeights);

    JButton recurringSameWeights = new JButton("Recurring Strategy with Same Weights");
    recurringSameWeights.addActionListener(e -> {
      PersistRecurrentStrategyWithSameWeightsForm persistRecurrentStrategyWithSameWeightsForm =
              new PersistRecurrentStrategyWithSameWeightsForm(mainForm, features);
      Utils.showPrevious(persistRecurrentStrategyWithSameWeightsForm, this);
    });
    this.add(recurringSameWeights);
  }
}
