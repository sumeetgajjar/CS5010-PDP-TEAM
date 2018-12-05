package virtualgambling.view.guiview;

import javax.swing.*;

import virtualgambling.controller.Features;

/**
 * Created by gajjar.s, on 4:06 AM, 12/5/18
 */
public class PlotPortfolioPerformance extends AbstractForm {

  private final Features features;

  protected PlotPortfolioPerformance(JFrame previousJFrame, Features features) {
    super(previousJFrame);
    this.features = features;
  }

  @Override
  protected void initComponents() {

  }
}
