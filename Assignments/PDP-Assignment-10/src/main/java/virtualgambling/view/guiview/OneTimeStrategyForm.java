package virtualgambling.view.guiview;

import javax.swing.*;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 2:12 AM, 12/6/18
 */
public class OneTimeStrategyForm extends AbstractForm {

  private final Features features;

  public OneTimeStrategyForm(MainForm mainForm, Features features) {
    super(mainForm);
    this.features = features;
  }

  @Override
  protected void initComponents() {
    this.add(new JLabel("One Time"));
  }
}
