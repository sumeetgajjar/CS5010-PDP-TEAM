package virtualgambling.view.guiview;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 1:52 AM, 12/6/18
 */
public class SelectStrategyForm extends AbstractForm {

  private final MainForm mainForm;
  private final Features features;

  protected SelectStrategyForm(MainForm mainForm, Features features) {
    super(mainForm);
    this.mainForm = mainForm;
    this.features = features;
  }

  @Override
  protected void initComponents() {

  }
}
