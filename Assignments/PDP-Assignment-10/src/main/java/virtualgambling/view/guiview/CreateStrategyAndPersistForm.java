package virtualgambling.view.guiview;

import java.awt.GridLayout;

import javax.swing.JButton;

import util.Utils;
import virtualgambling.controller.Features;

/**
 * This class represents a GUI form to create strategy and persists it to a file. It extends {@link
 * AbstractForm} to reduce the effort in implementing the common functionality.
 */
public class CreateStrategyAndPersistForm extends AbstractForm {

  private final MainForm mainForm;
  private final Features features;

  /**
   * Constructs a object of CreateStrategyAndPersistForm with the given params.
   *
   * @param mainForm the mainForm
   * @param features the features
   * @throws IllegalArgumentException if the given params are null
   */
  public CreateStrategyAndPersistForm(MainForm mainForm, Features features)
          throws IllegalArgumentException {
    super(mainForm);
    this.mainForm = Utils.requireNonNull(mainForm);
    this.features = Utils.requireNonNull(features);
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
